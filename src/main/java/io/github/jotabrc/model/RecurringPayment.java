package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public final class RecurringPayment extends Recurrence {

    private String payee;

    public RecurringPayment(long id, String uuid, UserFinance userFinance, LocalDate dueDate, double amount, String description, LocalDateTime createdAt, LocalDateTime updatedAt, long version, LocalDate recurringUntil, String payee) {
        super(id, userFinance, dueDate, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.payee = payee;
    }
}
