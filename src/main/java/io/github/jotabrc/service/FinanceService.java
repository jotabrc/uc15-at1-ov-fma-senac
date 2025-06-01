package io.github.jotabrc.service;


import io.github.jotabrc.dto.Page;
import io.github.jotabrc.dto.UserFinanceDto;

import java.sql.Connection;
import java.time.LocalDate;

public sealed interface FinanceService permits FinanceServiceImpl {

    void save(String userUuid, Connection conn);
    Page<UserFinanceDto> get(String userId,
                             int pageNumber,
                             int pageSize,
                             String sort,
                             LocalDate fromDate,
                             LocalDate toDate);
}
