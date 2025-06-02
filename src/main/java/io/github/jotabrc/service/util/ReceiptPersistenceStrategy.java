package io.github.jotabrc.service.util;

import io.github.jotabrc.model.Receipt;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import static io.github.jotabrc.util.TableName.TB_RECEIPT;

public class ReceiptPersistenceStrategy implements FinancePersistenceStrategy<Receipt> {

    private final PrepareStatement prepareStatement;
    private final SqlBuilder sqlBuilder;

    public ReceiptPersistenceStrategy() {
        this.sqlBuilder = DependencySelectorImpl.getInstance().select(SqlBuilder.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
    }

    @Override
    public LinkedHashMap<TableName, LinkedHashMap<String, Object>> generateMap(final Receipt receipt) {
        LinkedHashMap<TableName, LinkedHashMap<String, Object>> tableMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> tb = new LinkedHashMap<>();

        tb.put("uuid", receipt.getUuid());
        tb.put("vendor", receipt.getVendor());

        tableMap.put(TB_RECEIPT, tb);
        tableMap.put(TableName.TB_FINANCIAL_ENTITY, FinancialEntityPersistenceStrategyHelper.generateMap(receipt));
        return tableMap;
    }

    @Override
    public List<PreparedStatement> generateStatements(
            final Connection conn,
            final LinkedHashMap<TableName, LinkedHashMap<String, Object>> columnsAndValues,
            final LinkedHashMap<String, Object> conditions,
            final DQML dqml) throws SQLException {

        String tb = sqlBuilder.build(
                dqml.getType(),
                TB_RECEIPT.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TB_RECEIPT) : null,
                conditions
        );
        PreparedStatement ps1 = conn.prepareStatement(tb);
        prepareStatement.prepare(
                ps1,
                columnsAndValues != null ? columnsAndValues.get(TB_RECEIPT) : conditions);

        String tbFinancialEntity = sqlBuilder.build(
                dqml.getType(),
                TableName.TB_FINANCIAL_ENTITY.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_FINANCIAL_ENTITY) : null,
                conditions
        );
        PreparedStatement ps2 = conn.prepareStatement(tbFinancialEntity);
        prepareStatement.prepare(
                ps2,
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_FINANCIAL_ENTITY) : conditions);
        return List.of(ps1, ps2);
    }
}
