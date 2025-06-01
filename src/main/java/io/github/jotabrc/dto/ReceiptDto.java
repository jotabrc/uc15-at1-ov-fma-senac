package io.github.jotabrc.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public final class ReceiptDto extends FinancialEntityDto implements Serializable {

    private final String vendor;

    public ReceiptDto(
            LocalDate dueDate,
            double amount,
            String description,
            String vendor) {
        super(dueDate, amount, description);
        this.vendor = vendor;
    }
}
