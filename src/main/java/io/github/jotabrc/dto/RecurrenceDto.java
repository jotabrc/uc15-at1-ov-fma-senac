package io.github.jotabrc.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public sealed abstract class RecurrenceDto extends FinancialEntityDto implements Serializable permits RecurringPaymentDto, RecurringReceiptDto {

    private final LocalDate recurringUntil;

    public RecurrenceDto(
            LocalDate dueDate,
            double amount,
            String description,
            LocalDate recurringUntil) {
        super(dueDate, amount, description);
        this.recurringUntil = recurringUntil;
    }
}
