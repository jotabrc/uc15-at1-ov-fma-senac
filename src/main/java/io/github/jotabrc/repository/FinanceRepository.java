package io.github.jotabrc.repository;

import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.model.UserFinance;
import io.github.jotabrc.repository.util.DQML;

import java.sql.Connection;
import java.util.Map;
import java.util.Optional;

public interface FinanceRepository {

    void save(String userUuid, DQML dqml, Connection conn);
    Map<String, String> findByUserUuid(String userUuid);
    Optional<String> getFinancialEntityUserUuid(String financialEntityUuid);
    Optional<UserFinance> findByFilter(PageFilter pageFilter);
}
