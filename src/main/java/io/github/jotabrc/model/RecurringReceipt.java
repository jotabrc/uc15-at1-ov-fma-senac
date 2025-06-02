package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public final class RecurringReceipt extends Recurrence {

    private String vendor;

    public RecurringReceipt(String uuid,
                            String userFinanceUuid,
                            LocalDate dueDate,
                            double amount,
                            String description,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt,
                            long version,
                            LocalDate recurringUntil,
                            String vendor) {
        super(uuid, userFinanceUuid, dueDate, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.vendor = vendor;
    }
}
