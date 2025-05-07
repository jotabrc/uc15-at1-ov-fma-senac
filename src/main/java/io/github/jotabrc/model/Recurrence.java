package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Recurrence extends FinancialEntity {

    private LocalDate recurringUntil;

    public Recurrence(long id, String uuid, long userId, BigDecimal amount, String description, LocalDateTime createdAt,
                      LocalDateTime updatedAt, long version, LocalDate recurringUntil) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version);
        this.recurringUntil = recurringUntil;
    }
}
