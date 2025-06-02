package io.github.jotabrc.service.util;

import io.github.jotabrc.model.*;
import io.github.jotabrc.repository.util.DQML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProcessPersistenceStrategy {

    public static List<PreparedStatement> generateStatements(Connection conn, FinancialEntity entity, DQML dqml) throws SQLException {
        return switch (entity) {
            case Payment t -> FinancialEntityPersistenceStrategyHelper.generateStatements(conn, new PaymentPersistenceStrategy(), t, dqml);
            case Receipt t -> FinancialEntityPersistenceStrategyHelper.generateStatements(conn, new ReceiptPersistenceStrategy(), t, dqml);
            case RecurringPayment t -> FinancialEntityPersistenceStrategyHelper.generateStatements(conn, new RecurringPaymentPersistenceStrategy(), t, dqml);
            case RecurringReceipt t -> FinancialEntityPersistenceStrategyHelper.generateStatements(conn, new RecurringReceiptPersistenceStrategy(), t, dqml);
            case null, default -> throw new IllegalArgumentException("Invalid entity type, requires type extends FinancialEntity");
        };
    }
}
