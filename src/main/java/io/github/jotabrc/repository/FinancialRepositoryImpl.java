package io.github.jotabrc.repository;

import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.service.util.ProcessPersistenceStrategy;
import io.github.jotabrc.util.ConnectionUtil;
import io.github.jotabrc.util.DependencySelectorImpl;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class FinancialRepositoryImpl implements FinancialRepository {

    private final ConnectionUtil connectionUtil;

    public FinancialRepositoryImpl() {
        this.connectionUtil = DependencySelectorImpl.getInstance().select(ConnectionUtil.class);
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
}
