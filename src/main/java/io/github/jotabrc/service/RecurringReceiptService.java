package io.github.jotabrc.service;

import io.github.jotabrc.dto.RecurringPaymentDto;

public sealed interface RecurringReceiptService permits RecurringReceiptServiceImpl {

    String save(RecurringPaymentDto dto);
    void update(RecurringPaymentDto dto);
    void delete(String uuid);
}
