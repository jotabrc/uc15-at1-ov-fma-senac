package io.github.jotabrc.util;

import io.github.jotabrc.model.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper {

    private ResultSetMapper() {
    }

    public static UserFinance getUserFinance(final ResultSet rs, final String userUuid) throws SQLException {
        List<FinancialEntity> financialItems = new ArrayList<>();
        while(rs.next()) {
            financialItems.add(getFinancialEntity(rs));
        }

        return UserFinance
                .builder()
                .userUuid(userUuid)
                .financialItems(financialItems)
                .build();
    }

    public static FinancialEntity getFinancialEntity(final ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        String entityName = null;
        boolean isRecurring = false;
        if (hasColumn(metaData, "payee")) entityName = "PAYMENT";
        if (hasColumn(metaData, "vendor")) entityName = "RECEIPT";
        if (hasColumn(metaData, "recurring_until")) isRecurring = true;

        return switch (entityName) {
            case "PAYMENT" -> {
                if (isRecurring) yield getRecurringPayment(rs);
                else yield getPayment(rs);
            }
            case "RECEIPT" -> {
                if (isRecurring) yield getRecurringReceipt(rs);
                else yield getReceipt(rs);
            }
            case null, default -> throw new IllegalArgumentException("Required fields for mapping entity not found");
        };
    }

    private static boolean hasColumn(ResultSetMetaData metaData, String columnName) throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) return true;
        }
        return false;
    }

    public static Payment getPayment(final ResultSet rs) throws SQLException {
        return new Payment(
                null,
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                null,
                null,
                0,
                rs.getString("payee"));
    }

    public static Receipt getReceipt(final ResultSet rs) throws SQLException {
        return new Receipt(
                null,
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                null,
                null,
                0,
                rs.getString("vendor"));
    }

    public static RecurringPayment getRecurringPayment(final ResultSet rs) throws SQLException {
        return new RecurringPayment(
                null,
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                null,
                null,
                0,
                rs.getDate("recurring_until").toLocalDate(),
                rs.getString("payee"));
    }

    public static RecurringReceipt getRecurringReceipt(final ResultSet rs) throws SQLException {
        return new RecurringReceipt(
                null,
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                null,
                null,
                0,
                rs.getDate("recurring_until").toLocalDate(),
                rs.getString("vendor"));
    }
}
