package io.github.jotabrc.service.util;

import io.github.jotabrc.model.RecurringReceipt;
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

public class RecurringReceiptPersistenceStrategy implements FinancePersistenceStrategy<RecurringReceipt> {

    private final PrepareStatement prepareStatement;
    private final SqlBuilder sqlBuilder;

    public RecurringReceiptPersistenceStrategy() {
        this.sqlBuilder = DependencySelectorImpl.getInstance().select(SqlBuilder.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
    }

    @Override
    public LinkedHashMap<TableName, LinkedHashMap<String, Object>> generateMap(final RecurringReceipt recurringReceipt) {
        LinkedHashMap<TableName, LinkedHashMap<String, Object>> tableMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> tb = new LinkedHashMap<>();
        LinkedHashMap<String, Object> tbRecurrence = new LinkedHashMap<>();

        tb.put("uuid", recurringReceipt.getUuid());
        tb.put("vendor", recurringReceipt.getVendor());

        tbRecurrence.put("uuid", recurringReceipt.getUuid());
        tbRecurrence.put("recurring_until", recurringReceipt.getRecurringUntil());

        tableMap.put(TableName.TB_RECURRING_RECEIPT, tb);
        tableMap.put(TableName.TB_RECURRENCE, tbRecurrence);
        tableMap.put(TableName.TB_FINANCIAL_ENTITY, FinancialEntityPersistenceStrategyHelper.generateMap(recurringReceipt));
        return tableMap;
    }

    @Override
    public List<PreparedStatement> generateStatements(
            final Connection conn,
            final LinkedHashMap<TableName, LinkedHashMap<String, Object>> columnsAndValues,
            final LinkedHashMap<String, Object> conditions,
            final DQML dqml) throws SQLException {

        // RECURRING RECEIPT TABLE
        String tb = sqlBuilder.build(
                dqml.getType(),
                TableName.TB_RECURRING_RECEIPT.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_RECURRING_RECEIPT) : null,
                conditions
        );
        PreparedStatement ps1 = conn.prepareStatement(tb);
        prepareStatement.prepare(
                ps1,
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_RECURRING_RECEIPT) : conditions);

        // RECURRENCE TABLE
        String tbRecurrence = sqlBuilder.build(
                dqml.getType(),
                TableName.TB_RECURRENCE.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_RECURRENCE) : null,
                conditions
        );
        PreparedStatement ps2 = conn.prepareStatement(tbRecurrence);
        prepareStatement.prepare(
                ps2,
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_RECURRENCE) : conditions);

        // FINANCIAL ENTITY TABLE
        String tbFinancialEntity = sqlBuilder.build(
                dqml.getType(),
                TableName.TB_FINANCIAL_ENTITY.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_FINANCIAL_ENTITY) : null,
                conditions
        );
        PreparedStatement ps3 = conn.prepareStatement(tbFinancialEntity);
        prepareStatement.prepare(
                ps3,
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_FINANCIAL_ENTITY) : conditions);
        return List.of(ps1, ps2, ps3);
    }
}
