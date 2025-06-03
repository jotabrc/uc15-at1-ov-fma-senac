package io.github.jotabrc.service.util;

import io.github.jotabrc.model.Payment;
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

public class PaymentPersistenceStrategy implements FinancePersistenceStrategy<Payment> {

    private final PrepareStatement prepareStatement;
    private final SqlBuilder sqlBuilder;

    public PaymentPersistenceStrategy() {
        this.sqlBuilder = DependencySelectorImpl.getInstance().select(SqlBuilder.class);
        this.prepareStatement = DependencySelectorImpl.getInstance().select(PrepareStatement.class);
    }

    @Override
    public LinkedHashMap<TableName, LinkedHashMap<String, Object>> generateMap(final Payment payment) {
        LinkedHashMap<TableName, LinkedHashMap<String, Object>> tableMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> tb = new LinkedHashMap<>();

        tb.put("uuid", payment.getUuid());
        tb.put("payee", payment.getPayee());

        tableMap.put(TableName.TB_PAYMENT, tb);
        tableMap.put(TableName.TB_FINANCIAL_ENTITY, FinancialEntityPersistenceStrategyHelper.generateMap(payment));
        return tableMap;
    }

    @Override
    public List<PreparedStatement> generateStatements(
            final Connection conn,
            final LinkedHashMap<TableName, LinkedHashMap<String, Object>> columnsAndValues,
            final LinkedHashMap<String, Object> conditions,
            final DQML dqml) throws SQLException {

        String tbFinancialEntity = sqlBuilder.build(
                dqml.getType(),
                TableName.TB_FINANCIAL_ENTITY.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_FINANCIAL_ENTITY) : null,
                conditions
        );
        PreparedStatement ps1 = conn.prepareStatement(tbFinancialEntity);
        prepareStatement.prepare(
                ps1,
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_FINANCIAL_ENTITY) : conditions);

        String tbPayment = sqlBuilder.build(
                dqml.getType(),
                TableName.TB_PAYMENT.getTable(),
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_PAYMENT) : null,
                conditions
        );
        PreparedStatement ps2 = conn.prepareStatement(tbPayment);
        prepareStatement.prepare(
                ps2,
                columnsAndValues != null ? columnsAndValues.get(TableName.TB_PAYMENT) : conditions);
        return List.of(ps1, ps2);
    }
}
