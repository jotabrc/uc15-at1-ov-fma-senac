package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class RecurringReceipt extends Recurrence {

    private String vendor;

    public RecurringReceipt(long id, String uuid, long userId, BigDecimal amount, String description, LocalDateTime createdAt,
                            LocalDateTime updatedAt, long version, LocalDate recurringUntil, String vendor) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.vendor = vendor;
    }
}
