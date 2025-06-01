package io.github.jotabrc.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public sealed abstract class FinancialEntityDto implements Serializable
        permits PaymentDto, ReceiptDto, RecurrenceDto {

    private final LocalDate dueDate;
    private final double amount;
    private final String description;

    public FinancialEntityDto(
            LocalDate dueDate,
            double amount,
            String description) {
        this.dueDate = dueDate;
        this.amount = amount;
        this.description = description;
    }
}
