package io.github.jotabrc.service;

import io.github.jotabrc.dto.PaymentDto;

public sealed interface PaymentService permits PaymentServiceImpl {

    String save(PaymentDto dto);
    void update(PaymentDto dto);
    void delete(String uuid);
}
