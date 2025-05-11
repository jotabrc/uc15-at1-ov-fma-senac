package io.github.jotabrc.repository;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.security.ApplicationContext;
import io.github.jotabrc.util.ConnectionUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final ConnectionUtil connectionUtil;
    private final SqlBuilder sqlBuilder;
    private final PrepareStatement prepareStatement;
    private final RoleRepository roleRepository;
    private final ApplicationContext applicationContext;

    public UserRepositoryImpl() {
        this.connectionUtil = DependencyInjection.createConnectionUtil();
        this.sqlBuilder = DependencyInjection.createSqlBuilder();
        this.prepareStatement = DependencyInjection.createPrepareStatement();
        this.roleRepository = DependencyInjection.createRoleRepository();
        this.applicationContext = ApplicationContext.getInstance();
    }

    @Override
    public String save(User user, DQML dqml) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> columnsAndValues = getColumnsAndValues(user);
            String sql = "";
            switch (dqml) {
                case INSERT -> sql = sqlBuilder.build(dqml.getType(), "tb_user", columnsAndValues);
                case UPDATE -> {
                    String uuid = applicationContext.getUserUuid().orElseThrow(() -> new RuntimeException("User UUID not found"));
                    LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
                    conditions.put("uuid", uuid);
                    sql = sqlBuilder.build(dqml.getType(), "tb_user", columnsAndValues, conditions);
                }
            }
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
    public Optional<User> findByUuid(String uuid) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("uuid", uuid);
            String sql = sqlBuilder.build(DQML.SELECT.getType(), "tb_user", conditions, new String[]{"*"});
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps, conditions);
                try (ResultSet rs = ps.executeQuery()) {
                    User user = null;
                    user = buildUser(rs);
                    return Optional.ofNullable(user);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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

    private User buildUser(ResultSet rs) throws Exception {
        if (rs.next()) {
            Role role = getRoleUser(rs.getLong("role_id"));
            return User
                    .builder()
                    .id(rs.getLong("id"))
                    .uuid(rs.getString("uuid"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .name(rs.getString("name"))
                    .role(role)
                    .salt(null)
                    .hash(null)
                    .isActive(rs.getBoolean("is_active"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime().atZone(ZoneId.of("UTC")))
                    .updatedAt(
                            rs.getTimestamp("updated_at") != null ?
                                    rs.getTimestamp("updated_at").toLocalDateTime().atZone(ZoneId.of("UTC")) :
                                    null
                    )
                    .version(rs.getInt("version"))
                    .build();
        }
        return null;
    }

    private Role getRoleUser(final long id) throws Exception {
        return roleRepository.findById(id)
                .orElseThrow(() -> new Exception("Role with ID %d not found".formatted(id)));
    }
}
