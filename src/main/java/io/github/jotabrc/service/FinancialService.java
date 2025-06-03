package io.github.jotabrc.service;

import io.github.jotabrc.dto.FinancialEntityDto;
import io.github.jotabrc.model.FinancialEntity;

import java.util.Optional;

public sealed interface FinancialService permits FinancialServiceImpl {

    void save(FinancialEntityDto dto);
    void update(String uuid, FinancialEntityDto dto);
    void delete(String uuid);
    Optional<FinancialEntity> findByUuid(String uuid);
}
