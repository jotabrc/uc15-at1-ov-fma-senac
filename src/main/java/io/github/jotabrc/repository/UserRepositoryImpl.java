package io.github.jotabrc.repository;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.util.ConnectionUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;

public class UserRepositoryImpl implements UserRepository {

    private final ConnectionUtil connectionUtil;
    private final SqlBuilder sqlBuilder;
    private final PrepareStatement prepareStatement;

    public UserRepositoryImpl() {
        this.connectionUtil = DependencyInjection.createConnectionUtil();
        this.sqlBuilder = DependencyInjection.createSqlBuilder();
        this.prepareStatement = DependencyInjection.createPrepareStatement();
    }

    @Override
    public String save(final User user) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> columnsAndValues = getColumnsAndValues(user);
            String sql = sqlBuilder.build(DQML.INSERT.getType(), "tb_user", columnsAndValues);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                prepareStatement.prepare(ps, columnsAndValues);
                ps.executeUpdate();
                conn.commit();
                conn.setAutoCommit(true);
                return user.getUuid();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("username", username);

            String sql = sqlBuilder.build(DQML.SELECT.getType(), "tb_user", conditions, new String[]{"username"});
            return executeStatement(conditions, conn, sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("email", email);

            String sql = sqlBuilder.build(DQML.SELECT.getType(), "tb_user", conditions, new String[]{"username"});
            return executeStatement(conditions, conn, sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private LinkedHashMap<String, Object> getColumnsAndValues(final User user) {
        LinkedHashMap<String, Object> columnsAndValues = new LinkedHashMap<>();
        columnsAndValues.put("uuid", user.getUuid());
        columnsAndValues.put("username", user.getUsername());
        columnsAndValues.put("email", user.getEmail());
        columnsAndValues.put("name", user.getName());
        columnsAndValues.put("role_id", user.getRole().getId());
        columnsAndValues.put("salt", user.getSalt());
        columnsAndValues.put("hash", user.getHash());
        columnsAndValues.put("is_active", user.isActive());
        columnsAndValues.put("created_at", Timestamp.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()));
        columnsAndValues.put("version", 0);
        return columnsAndValues;
    }

    private boolean executeStatement(LinkedHashMap<String, Object> columnsAndValues, Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            prepareStatement.prepare(ps, columnsAndValues);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }
}
