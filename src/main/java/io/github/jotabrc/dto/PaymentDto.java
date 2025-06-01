package io.github.jotabrc.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public final class PaymentDto extends FinancialEntityDto implements Serializable {

    private final String payee;

    public PaymentDto(
            LocalDate dueDate,
            double amount,
            String description,
            String payee) {
        super(dueDate, amount, description);
        this.payee = payee;
    }
}
