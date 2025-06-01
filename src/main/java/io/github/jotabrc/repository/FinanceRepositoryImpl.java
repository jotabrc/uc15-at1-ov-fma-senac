package io.github.jotabrc.repository;

import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.model.UserFinance;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.DependencySelectorImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        String sql = "";
        switch (dqml) {
            case INSERT -> sql = sqlBuilder.build(dqml.getType(), "tb_user_finance", columnsAndValues);
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
    public Optional<UserFinance> findByUserUuid(final String userUuid) {
        return Optional.empty();
    }

    @Override
    public List<UserFinance> findByFilter(final PageFilter pageFilter) {
        return List.of();
    }

    private void getColumnsAndValues(final String userUuid, final Map<String, Object> columnsAndValues) {
        columnsAndValues.put("user_uuid", userUuid);
        columnsAndValues.put("created_at", Timestamp.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()));
        columnsAndValues.put("version", 0);
    }
}
