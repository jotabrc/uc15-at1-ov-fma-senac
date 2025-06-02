package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public final class Receipt extends FinancialEntity {

    private String vendor;

    public Receipt(String uuid, String userFinanceUuid, LocalDate dueDate, double amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String vendor) {
        super(uuid, userFinanceUuid, dueDate, amount, description, createdAt, updatedAt, version);
        this.vendor = vendor;
    }
}
