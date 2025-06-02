package io.github.jotabrc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public sealed abstract class FinancialEntity
        permits Payment, Receipt, Recurrence {

    private String uuid;
    private String userFinanceUuid;
    private LocalDate dueDate;
    private double amount;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long version;
}
