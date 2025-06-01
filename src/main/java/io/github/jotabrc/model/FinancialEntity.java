package io.github.jotabrc.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Accessors(chain = true)
@Builder
public sealed abstract class FinancialEntity
        permits Payment, Receipt, Recurrence {

    private final long id;
    private final UserFinance userFinance;
    private final LocalDate dueDate;
    private final double amount;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final long version;
}
