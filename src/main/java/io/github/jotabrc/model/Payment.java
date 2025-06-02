package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public final class Payment extends FinancialEntity {

    private String payee;

    public Payment(String uuid, String userFinanceUuid, LocalDate dueDate, double amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String payee) {
        super(uuid, userFinanceUuid, dueDate, amount, description, createdAt, updatedAt, version);
        this.payee = payee;
    }
}
