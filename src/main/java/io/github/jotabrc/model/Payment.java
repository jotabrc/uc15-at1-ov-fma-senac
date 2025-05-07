package io.github.jotabrc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Payment extends FinancialEntity {

    private String payee;

    public Payment(long id, String uuid, long userId, BigDecimal amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String payee) {
        super(id, uuid, userId, amount, description, createdAt, updatedAt, version);
        this.payee = payee;
    }
}
