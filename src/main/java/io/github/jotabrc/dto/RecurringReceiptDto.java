package io.github.jotabrc.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public final class RecurringReceiptDto extends RecurrenceDto implements Serializable {

    private final String vendor;

    public RecurringReceiptDto(
            LocalDate dueDate,
            double amount,
            String description,
            LocalDate recurringUntil,
            String vendor) {
        super(dueDate, amount, description, recurringUntil);
        this.vendor = vendor;
    }
}
