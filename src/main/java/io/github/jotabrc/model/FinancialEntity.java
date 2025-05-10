package io.github.jotabrc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public abstract class FinancialEntity {

    private long id;
    private String uuid;
    private long userId;
    private BigDecimal amount;
    private String description;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private long version;
}
