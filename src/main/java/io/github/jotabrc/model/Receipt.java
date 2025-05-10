package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class Receipt extends FinancialEntity {

    private String vendor;

    public Receipt(long id, String uuid, long userId, BigDecimal amount, String description, ZonedDateTime createdAt,
                   ZonedDateTime updatedAt, long version, String vendor) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version);
        this.vendor = vendor;
    }
}
