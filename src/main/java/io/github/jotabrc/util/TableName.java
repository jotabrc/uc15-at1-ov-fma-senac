package io.github.jotabrc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableName {

    TB_USER_FINANCE("tb_user_finance"),
    TB_FINANCIAL_ENTITY("tb_financial_entity"),
    TB_PAYMENT("tb_payment"),
    TB_RECEIPT("tb_receipt"),
    TB_RECURRING_PAYMENT("tb_recurring_payment"),
    TB_RECURRING_RECEIPT("tb_recurring_receipt"),
    TB_RECURRENCE("tb_recurrence");

    private final String table;
}
