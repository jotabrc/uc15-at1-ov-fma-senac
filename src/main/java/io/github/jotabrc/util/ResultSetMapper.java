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
            financialItems.add(getEntity(rs));
        }

        return UserFinance
                .builder()
                .userUuid(userUuid)
                .financialItems(financialItems)
                .build();
    }

    public static FinancialEntity getFinancialEntity(final ResultSet rs) throws SQLException {
        if (rs.next()) return getEntity(rs);
        else throw new IllegalArgumentException("Result Set is empty");
    }

    public static FinancialEntity getEntity(final ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        String entityName = null;
        if (rs.getString("p_payee") != null) entityName = "PAYMENT";
        if (rs.getString("r_vendor") != null) entityName = "RECEIPT";
        if (rs.getString("recurring_vendor") != null) entityName = "RECURRING_RECEIPT";
        if (rs.getString("recurring_payee") != null) entityName = "RECURRING_PAYMENT";

        return switch (entityName) {
            case "PAYMENT" -> getPayment(rs);
            case "RECEIPT" -> getReceipt(rs);
            case "RECURRING_RECEIPT" -> getRecurringReceipt(rs);
            case "RECURRING_PAYMENT" -> getRecurringPayment(rs);
            case null, default -> throw new IllegalArgumentException("Required fields for mapping entity not found");
        };
    }

    public static Payment getPayment(final ResultSet rs) throws SQLException {
        return new Payment(
                rs.getString("uuid"),
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("version"),
                rs.getString("p_payee"));
    }

    public static Receipt getReceipt(final ResultSet rs) throws SQLException {
        return new Receipt(
                rs.getString("uuid"),
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("version"),
                rs.getString("r_vendor"));
    }

    public static RecurringPayment getRecurringPayment(final ResultSet rs) throws SQLException {
        return new RecurringPayment(
                rs.getString("uuid"),
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("version"),
                rs.getDate("recurring_until").toLocalDate(),
                rs.getString("recurring_payee"));
    }

    public static RecurringReceipt getRecurringReceipt(final ResultSet rs) throws SQLException {
        return new RecurringReceipt(
                rs.getString("uuid"),
                null,
                rs.getDate("due_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("version"),
                rs.getDate("recurring_until").toLocalDate(),
                rs.getString("recurring_vendor")
        );
    }
}
