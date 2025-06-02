package io.github.jotabrc.repository;

import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.repository.util.DQML;

public interface FinancialRepository {

    void persist(FinancialEntity entity, DQML dqml);
}
