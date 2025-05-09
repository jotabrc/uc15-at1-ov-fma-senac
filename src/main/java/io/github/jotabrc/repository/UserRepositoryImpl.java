package io.github.jotabrc.repository;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.model.User;
import io.github.jotabrc.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UserRepositoryImpl implements UserRepository {

    private final ConnectionUtil connectionUtil;

    public UserRepositoryImpl() {
        this.connectionUtil = DependencyInjection.createConnectionUtil();
    }

    @Override
    public String add(final User user) {
        try (Connection conn = connectionUtil.getCon()) {
            String insertUser = "INSERT INTO tb_user " +
                    "(uuid, username, email, email, name, role, salt, hash, is_active, created_at, version)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
                ps.setString(1, user.getUuid());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getName());
                ps.setLong(5, user.getRole().getId());
                ps.setString(6, user.getSalt());
                ps.setString(7, user.getHash());
                ps.setBoolean(8, user.isActive());
                ps.setTimestamp(9, Timestamp.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()));
                ps.setLong(10, 0);
                ps.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
