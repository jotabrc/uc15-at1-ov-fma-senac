package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public sealed abstract class Recurrence extends FinancialEntity permits RecurringPayment, RecurringReceipt {

    private LocalDate recurringUntil;

    public Recurrence(String uuid,
                      String userFinanceUuid,
                      LocalDate dueDate,
                      double amount,
                      String description,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      long version,
                      LocalDate recurringUntil) {
        super(uuid, userFinanceUuid, dueDate, amount, description, createdAt, updatedAt, version);
        this.recurringUntil = recurringUntil;
    }
}
