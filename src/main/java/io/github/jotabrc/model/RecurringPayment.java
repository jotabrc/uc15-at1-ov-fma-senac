package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class RecurringPayment extends Recurrence {

    private String payee;

    public RecurringPayment(long id, String uuid, long userId, BigDecimal amount, String description, ZonedDateTime createdAt,
                            ZonedDateTime updatedAt, long version, ZonedDateTime recurringUntil, String payee) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.payee = payee;
    }
}
