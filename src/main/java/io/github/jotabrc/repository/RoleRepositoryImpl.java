package io.github.jotabrc.repository;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.util.ConnectionUtil;
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
        this.connectionUtil = DependencyInjection.createConnectionUtil();
        this.sqlBuilder = DependencyInjection.createSqlBuilder();
        this.prepareStatement = DependencyInjection.createPrepareStatement();
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
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    @Override
    public Optional<Role> findByName(String name) {
        try (Connection conn = connectionUtil.getCon()) {
            LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
            conditions.put("name", name);
            String sql = sqlBuilder.build(DQML.INSERT.getType(), "tb_role", conditions);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                prepareStatement.prepare(ps,conditions);
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    Role role = null;
                    while(rs.next()) {
                        role = buildRole(rs);
                    }
                    return Optional.ofNullable(role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Role buildRole(final ResultSet rs) throws SQLException {
        return Role
                .builder()
                .id(rs.getInt("id"))
                .uuid(rs.getString("uuid"))
                .name(RoleName.getRole(rs.getString("name")))
                .description(rs.getString("description"))
                .isActive(rs.getBoolean("is_active"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .version(rs.getInt("version"))
                .build();
    }

    private LinkedHashMap<String, Object> getColumnsAndValues(final Role role) {
        LinkedHashMap<String, Object> columnsAndValues = new LinkedHashMap<>();
        columnsAndValues.put("uuid", role.getUuid());
        columnsAndValues.put("name", role.getName());
        columnsAndValues.put("description", role.getDescription());
        columnsAndValues.put("is_active", role.isActive());
        columnsAndValues.put("created_at", role.getCreatedAt());
        columnsAndValues.put("version", role.getVersion());
        return columnsAndValues;
    }
}
