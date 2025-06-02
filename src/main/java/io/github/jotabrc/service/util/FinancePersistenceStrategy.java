package io.github.jotabrc.service.util;

import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

public interface FinancePersistenceStrategy<T extends FinancialEntity> {

    LinkedHashMap<TableName, LinkedHashMap<String, Object>> generateMap(T t);
    List<PreparedStatement> generateStatements(
            final Connection conn,
            final LinkedHashMap<TableName, LinkedHashMap<String, Object>> columnsAndValues,
            final LinkedHashMap<String, Object> conditions,
            final DQML dqml) throws SQLException;
}
