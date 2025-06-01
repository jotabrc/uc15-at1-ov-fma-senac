package io.github.jotabrc.service;

import io.github.jotabrc.dto.ReceiptDto;

public sealed interface ReceiptService permits ReceiptServiceImpl {

    String save(ReceiptDto dto);
    void update(ReceiptDto dto);
    void delete(String uuid);
}
