package io.github.jotabrc.service;

import io.github.jotabrc.dto.RecurringPaymentDto;

public sealed interface RecurringPaymentService permits RecurringPaymentServiceImpl {

    String save(RecurringPaymentDto dto);
    void update(RecurringPaymentDto dto);
    void delete(String uuid);
}
