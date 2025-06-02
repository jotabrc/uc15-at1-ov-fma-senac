package io.github.jotabrc.service.util;

import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FinancialEntityPersistenceStrategyHelper {

    public static LinkedHashMap<String, Object> generateMap(final FinancialEntity entity) {
        LinkedHashMap<String, Object> tbFinancialEntity = new LinkedHashMap<>();
        tbFinancialEntity.put("uuid", entity.getUuid());
        tbFinancialEntity.put("user_finance_uuid", entity.getUserFinanceUuid());
        tbFinancialEntity.put("due_date", entity.getDueDate());
        tbFinancialEntity.put("updated_at", entity.getUpdatedAt());
        tbFinancialEntity.put("amount", entity.getAmount());
        tbFinancialEntity.put("description", entity.getDescription());
        tbFinancialEntity.put("version", entity.getVersion());
        return tbFinancialEntity;
    }

    public static <T extends FinancialEntity> List<PreparedStatement> generateStatements(
            final Connection conn,
            final FinancePersistenceStrategy<T> financePersistenceStrategy,
            final T entity,
            final DQML dqml) throws SQLException {

        LinkedHashMap<TableName, LinkedHashMap<String, Object>> columnsAndValues = null;
        LinkedHashMap<String, Object> conditions = null;

        switch (dqml) {
            case INSERT -> {
                columnsAndValues = financePersistenceStrategy.generateMap(entity);
            }
            case UPDATE, DELETE -> {
                conditions = FinancialEntityPersistenceStrategyHelper.generateConditions(entity.getUuid());
            }
        }
        return financePersistenceStrategy.generateStatements(conn, columnsAndValues, conditions, dqml);
    }

    public static LinkedHashMap<String, Object> generateConditions(final String uuid) {
        return new LinkedHashMap<>(Map.of("uuid", uuid));
    }
}
