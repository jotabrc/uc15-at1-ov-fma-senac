package io.github.jotabrc.repository;

import io.github.jotabrc.model.Role;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.RoleName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;

public class RoleRepositoryImpl implements RoleRepository {

    private final ConnectionUtil connectionUtil;
    private final SqlBuilder sqlBuilder;
    private final PrepareStatement prepareStatement;

    public RoleRepositoryImpl() {
        this.connectionUtil = DependencySelectorImpl.getInstance().select(ConnectionUtil.class);
        this.sqlBuilder = DependencySelectorImpl.getInstance().select(SqlBuilder.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
    }

    @Override
    public String save(Role role) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> columnsAndValues = getColumnsAndValues(role);
            String sql = sqlBuilder.build(DQML.INSERT.getType(), "tb_role", columnsAndValues);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                prepareStatement.prepare(ps, columnsAndValues);
                ps.executeUpdate();
                conn.commit();
                conn.setAutoCommit(true);
                return role.getUuid();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Role> findByName(String name) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("name", name);
            String sql = sqlBuilder.build(DQML.SELECT.getType(), "tb_role", conditions, new String[]{"*"});
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps, conditions);
                try (ResultSet rs = ps.executeQuery()) {
                    Role role = null;
                    role = buildRole(rs);
                    return Optional.ofNullable(role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Role> findByUuid(String uuid) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("uuid", uuid);
            String sql = sqlBuilder.build(DQML.SELECT.getType(), "tb_role", conditions, new String[]{"*"});
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps, conditions);
                try (ResultSet rs = ps.executeQuery()) {
                    Role role;
                    role = buildRole(rs);
                    return Optional.ofNullable(role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("name", name);
            String sql = sqlBuilder.build(DQML.SELECT.getType(), "tb_role", conditions, new String[]{"name"});
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps, conditions);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Role buildRole(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            return Role
                    .builder()
                    .uuid(rs.getString("uuid"))
                    .name(RoleName.getRole(rs.getString("name")))
                    .description(rs.getString("description"))
                    .isActive(rs.getBoolean("is_active"))
                    .createdAt(
                            rs.getTimestamp("created_at") != null ?
                                    rs.getTimestamp("created_at").toLocalDateTime() :
                                    null
                    )
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

    private LinkedHashMap<String, Object> getColumnsAndValues(final Role role) {
        LinkedHashMap<String, Object> columnsAndValues = new LinkedHashMap<>();
        columnsAndValues.put("uuid", role.getUuid());
        columnsAndValues.put("name", role.getName());
        columnsAndValues.put("description", role.getDescription());
        columnsAndValues.put("is_active", role.isActive());
        columnsAndValues.put("updated_at", role.getUpdatedAt());
        columnsAndValues.put("version", role.getVersion());
        return columnsAndValues;
    }
}
