package io.github.jotabrc.repository;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.RoleName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRepositoryImpl implements RoleRepository {

    private final ConnectionUtil connectionUtil;

    public RoleRepositoryImpl() {
        this.connectionUtil = DependencyInjection.createConnectionUtil();
    }

    @Override
    public Role get(String name) {
        try (Connection conn = connectionUtil.getCon()) {
            String getRole = "SELECT * FROM tb_role AS r WHERE r.name = ?";
            try (PreparedStatement ps = conn.prepareStatement(getRole)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    Role role = null;
                    while(rs.next()) {
                        role = buildRole(rs);
                    }
                    return role;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Role buildRole(ResultSet rs) throws SQLException {
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
}
