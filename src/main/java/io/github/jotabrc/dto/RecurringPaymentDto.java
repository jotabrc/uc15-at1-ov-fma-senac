package io.github.jotabrc.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public final class RecurringPaymentDto extends RecurrenceDto implements Serializable {

    private final String payee;

    public RecurringPaymentDto(
            LocalDate dueDate,
            double amount,
            String description,
            LocalDate recurringUntil,
            String payee) {
        super(dueDate, amount, description, recurringUntil);
        this.payee = payee;
    }
}
