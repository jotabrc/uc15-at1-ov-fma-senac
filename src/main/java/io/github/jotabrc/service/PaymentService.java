package io.github.jotabrc.service;

import io.github.jotabrc.dto.PaymentDto;
import io.github.jotabrc.model.Payment;

import java.util.Optional;

public sealed interface PaymentService permits PaymentServiceImpl {

    void save(PaymentDto dto);
    void update(String uuid, PaymentDto dto);
    void delete(String uuid);
    Optional<Payment> findByUuid(String uuid);
}
