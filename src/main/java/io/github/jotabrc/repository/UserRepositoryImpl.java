package io.github.jotabrc.repository;

import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.security.ApplicationContextHolder;
import io.github.jotabrc.service.FinanceService;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.DependencySelectorImpl;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final ConnectionUtil connectionUtil;
    private final SqlBuilder sqlBuilder;
    private final PrepareStatement prepareStatement;
    private final ApplicationContextHolder applicationContext;
    private final RoleRepository roleRepository;
    private final FinanceService financeService;

    public UserRepositoryImpl() {
        this.connectionUtil = DependencySelectorImpl.getInstance().select(ConnectionUtil.class);
        this.sqlBuilder = DependencySelectorImpl.getInstance().select(SqlBuilder.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
        this.applicationContext = DependencySelectorImpl.getInstance().select(ApplicationContextHolder.class);
        this.roleRepository = DependencySelectorImpl.getInstance().select(RoleRepository.class);
        this.financeService = DependencySelectorImpl.getInstance().select(FinanceService.class);
    }

    @Override
    public String save(User user, DQML dqml) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> columnsAndValues = new LinkedHashMap<>();
            getColumnsAndValues(user, columnsAndValues);

            String sql = "";
            switch (dqml) {
                case INSERT -> sql = sqlBuilder.build(dqml.getType(), "tb_user", columnsAndValues);
                case UPDATE -> {
                    String uuid = Optional
                            .of(applicationContext.getContextDetail())
                            .orElseThrow(() -> new RuntimeException("User UUID not found"));
                    LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
                    conditions.put("uuid", uuid);
                    sql = sqlBuilder.build(dqml.getType(), "tb_user", columnsAndValues, conditions);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                prepareStatement.prepare(ps, columnsAndValues);
                ps.execute();
                financeService.save(user.getUuid(), conn);
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
    public Optional<User> findById(final long id) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("id", id);
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
    public Optional<User> findByUuid(final String uuid) {
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
    public Optional<User> findByEmail(String email) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("email", email);

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

    private void getColumnsAndValues(final User user, final LinkedHashMap<String, Object> columnsAndValues) {
        columnsAndValues.put("uuid", user.getUuid());
        columnsAndValues.put("username", user.getUsername());
        columnsAndValues.put("email", user.getEmail());
        columnsAndValues.put("name", user.getName());
        columnsAndValues.put("role_uuid", user.getRole().getUuid());
        columnsAndValues.put("salt", user.getSalt());
        columnsAndValues.put("hash", user.getHash());
        columnsAndValues.put("is_active", user.isActive());
        columnsAndValues.put("created_at", Timestamp.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()));
        columnsAndValues.put("version", 0);
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
            Role role = getRoleUser(rs.getString("role_uuid"));
            return User
                    .builder()
                    .uuid(rs.getString("uuid"))
                    .uuid(rs.getString("uuid"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .name(rs.getString("name"))
                    .role(role)
                    .salt(rs.getString("salt"))
                    .hash(rs.getString("hash"))
                    .isActive(rs.getBoolean("is_active"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(
                            rs.getTimestamp("updated_at") != null ?
                                    rs.getTimestamp("updated_at").toLocalDateTime() :
                                    null
                    )
                    .version(rs.getInt("version"))
                    .build();
        }
        return null;
    }

    private Role getRoleUser(final String uuid) throws Exception {
        return roleRepository.findByUuid(uuid)
                .orElseThrow(() -> new Exception("Role with UUID %d not found".formatted(uuid)));
    }
}
