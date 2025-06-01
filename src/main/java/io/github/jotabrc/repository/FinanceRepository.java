package io.github.jotabrc.repository;

import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.model.UserFinance;
import io.github.jotabrc.repository.util.DQML;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface FinanceRepository {

    void save(String userUuid, final DQML dqml, Connection conn);
    Optional<UserFinance> findByUserUuid(final String userUuid);
    List<UserFinance> findByFilter(final PageFilter pageFilter);
}
