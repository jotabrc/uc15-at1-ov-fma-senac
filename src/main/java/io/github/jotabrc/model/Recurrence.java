package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public abstract class Recurrence extends FinancialEntity {

    private ZonedDateTime recurringUntil;

    public Recurrence(long id, String uuid, long userId, BigDecimal amount, String description, ZonedDateTime createdAt,
                      ZonedDateTime updatedAt, long version, ZonedDateTime recurringUntil) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version);
        this.recurringUntil = recurringUntil;
    }
}
