package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class RecurringReceipt extends Recurrence {

    private String vendor;

    public RecurringReceipt(long id, String uuid, long userId, BigDecimal amount, String description, ZonedDateTime createdAt,
                            ZonedDateTime updatedAt, long version, ZonedDateTime recurringUntil, String vendor) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.vendor = vendor;
    }
}
