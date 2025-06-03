package io.github.jotabrc.repository;

import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.service.util.ProcessPersistenceStrategy;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.ResultSetMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FinancialRepositoryImpl implements FinancialRepository {

    private final ConnectionUtil connectionUtil;
    private final PrepareStatement prepareStatement;

    public FinancialRepositoryImpl() {
        this.connectionUtil = DependencySelectorImpl.getInstance().select(ConnectionUtil.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
    }

    @Override
    public void persist(final FinancialEntity entity, final DQML dqml) {
        try (Connection conn = connectionUtil.getCon()) {
            List<PreparedStatement> preparedStatements = ProcessPersistenceStrategy.generateStatements(conn, entity, dqml);
            conn.setAutoCommit(false);
            preparedStatements.forEach(preparedStatement -> {
                try {
                    preparedStatement.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (preparedStatement != null) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            try {
                                conn.rollback();
                            } catch (SQLException ex) {
                                log.warn("Error in PreparedStatement rollback: {}", e.getMessage());
                            }
                            log.warn("Error closing PreparedStatement: {}", e.getMessage());
                        }
                    }
                }
            });
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FinancialEntity> findByUuid(final String uuid) {
        try (Connection conn = connectionUtil.getCon()) {
            String sql = """
                    SELECT rec.recurring_until AS recurring, rp.payee AS recurring_payee, rr.vendor AS recurring_vendor, 
                    r.vendor AS receipt_vendor, p.payee AS payee, fe.amount, fe.due_date, fe.description, fe.uuid, fe.created_at, fe.updated_at
                    fe.version AS version
                    FROM tb_financial_entity fe
                    JOIN tb_payment p ON p.uuid = fe.uuid
                    JOIN tb_receipt r ON r.uuid = fe.uuid
                    JOIN tb_recurrence rec ON rec.uuid = fe.uuid
                    JOIN tb_recurring_receipt rr ON rr.uuid = fe.uuid
                    JOIN tb_recurring_payment rp ON rp.uuid = fe.uuid
                    WHERE fe.uuid = ?""";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                LinkedHashMap<String, Object> values = new LinkedHashMap<>();
                values.put("uuid", uuid);
                prepareStatement.prepare(ps, values);
                try (ResultSet rs = ps.executeQuery()) {
                    FinancialEntity entity = ResultSetMapper.getFinancialEntity(rs);
                    return Optional.ofNullable(entity);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
