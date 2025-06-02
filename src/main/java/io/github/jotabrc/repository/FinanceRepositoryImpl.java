package io.github.jotabrc.repository;

import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.handler.NotFoundException;
import io.github.jotabrc.model.UserFinance;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.ResultSetMapper;
import io.github.jotabrc.util.TableName;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class FinanceRepositoryImpl implements FinanceRepository {

    private final ConnectionUtil connectionUtil;
    private final SqlBuilder sqlBuilder;
    private final PrepareStatement prepareStatement;

    public FinanceRepositoryImpl() {
        this.connectionUtil = DependencySelectorImpl.getInstance().select(ConnectionUtil.class);
        this.sqlBuilder = DependencySelectorImpl.getInstance().select(SqlBuilder.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
    }

    @Override
    public void save(final String userUuid, final DQML dqml, Connection conn) {
        LinkedHashMap<String, Object> columnsAndValues = new LinkedHashMap<>();
        getColumnsAndValues(userUuid, columnsAndValues);

        String sql;
        switch (dqml) {
            case INSERT -> sql = sqlBuilder.build(
                    dqml.getType(),
                    TableName.TB_USER_FINANCE.getTable(),
                    columnsAndValues
            );
            case null, default -> throw new IllegalArgumentException("Operation not supported");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            prepareStatement.prepare(ps, columnsAndValues);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> findByUserUuid(final String userUuid) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("user_uuid", userUuid);
            String sql = sqlBuilder.build(
                    DQML.SELECT.getType(),
                    TableName.TB_USER_FINANCE.getTable(),
                    conditions,
                    new String[]{"uuid", "user_finance_uuid"}
            );
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps, conditions);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next())
                        return Map.of(
                                "uuid", rs.getString("uuid"),
                                "user_finance_uuid", rs.getString("user_finance_uuid")
                        );
                    throw new NotFoundException("User financial information not found with user UUID %s".formatted(userUuid));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> getFinancialEntityUserUuid(final String financialEntityUuid) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("uuid", financialEntityUuid);
            String sql = """
                    SELECT uf.user_uuid
                    FROM tb_user_finance  uf
                    JOIN tb_financial_entity fe ON fe.user_finance_uuid = uf.uuid
                    WHERE fe.uuid = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps, conditions);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next())
                        return Optional.ofNullable(rs.getString("user_uuid"));
                    throw new NotFoundException("User financial information not found with UUID %s".formatted(financialEntityUuid));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserFinance> findByFilter(final PageFilter pageFilter) {

        try (Connection conn = connectionUtil.getCon()) {
            String sql = """
                    SELECT rec.recurring_until, rp.payee, rr.vendor, r.vendor, p.payee, fe.amount, fe.due_date, fe.description
                    FROM tb_user_finance uf
                    JOIN tb_financial_entity fe ON fe.user_finance_uuid = uf.uuid
                    JOIN tb_payment p ON p.uuid = fe.uuid
                    JOIN tb_receipt r ON r.uuid = fe.uuid
                    JOIN tb_recurrence rec ON rec.uuid = fe.uuid
                    JOIN tb_recurring_receipt rr ON rr.uuid = fe.uuid
                    JOIN tb_recurring_payment rp ON rp.uuid = fe.uuid
                    WHERE fe.due_date BETWEEN ? AND ? AND
                    (rec.recurring_until BETWEEN ? AND ? OR rec.recurring_until IS NULL)
                    AND uf.user_uuid = ? ORDER BY %s LIMIT ? OFFSET ?""".formatted(pageFilter.sort());
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                LinkedHashMap<String, Object> values = new LinkedHashMap<>();
                getFilterValues(pageFilter, values);
                prepareStatement.prepare(ps, values);
                try (ResultSet rs = ps.executeQuery()) {
                    UserFinance finance = ResultSetMapper.getUserFinance(rs, pageFilter.userUuid());
                    return Optional.ofNullable(finance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getFilterValues(PageFilter pageFilter, LinkedHashMap<String, Object> values) {
        values.put("from_date", pageFilter.fromDate());
        values.put("to_date", pageFilter.toDate());
        values.put("recurring_from_date", pageFilter.fromDate());
        values.put("recurring_to_date", pageFilter.toDate());
        values.put("user_uui", pageFilter.userUuid());
        values.put("limit", pageFilter.pageSize());
        values.put("offset", pageFilter.pageNumber());
    }

    private void getColumnsAndValues(final String userUuid, final Map<String, Object> columnsAndValues) {
        columnsAndValues.put("uuid", UUID.randomUUID().toString());
        columnsAndValues.put("user_uuid", userUuid);
        columnsAndValues.put("created_at", Timestamp.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()));
        columnsAndValues.put("version", 0);
    }
}
