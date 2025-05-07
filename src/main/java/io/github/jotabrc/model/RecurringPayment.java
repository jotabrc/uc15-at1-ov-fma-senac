package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RecurringPayment extends Recurrence {

    private String payee;

    public RecurringPayment(long id, String uuid, long userId, BigDecimal amount, String description, LocalDateTime createdAt,
                            LocalDateTime updatedAt, long version, LocalDate recurringUntil, String payee) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.payee = payee;
    }
}
