package io.github.jotabrc.service;

import io.github.jotabrc.dto.Page;
import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.dto.UserFinanceDto;
import io.github.jotabrc.handler.NotFoundException;
import io.github.jotabrc.repository.FinanceRepository;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.security.ApplicationContextHolder;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.DtoMapper;
import io.github.jotabrc.util.PageMapper;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class FinanceServiceImpl implements FinanceService {

    private final ApplicationContextHolder applicationContextHolder;
    private final FinanceRepository financeRepository;

    public FinanceServiceImpl() {
        this.applicationContextHolder = DependencySelectorImpl.getInstance().select(ApplicationContextHolder.class);
        this.financeRepository = DependencySelectorImpl.getInstance().select(FinanceRepository.class);
    }

    @Override
    public void save(final String userUuid, Connection conn) {
        applicationContextHolder.hasAuthorization(userUuid);
        financeRepository.save(userUuid, DQML.INSERT, conn);
    }

    @Override
    public Page<UserFinanceDto> get(final String userUuid,
                                    final int pageNumber,
                                    final int pageSize,
                                    final String sort,
                                    final LocalDate fromDate,
                                    final LocalDate toDate) {
        applicationContextHolder.hasAuthorization(userUuid);
        PageFilter pageFilter = PageMapper.toPageFilter(userUuid, pageNumber, pageSize, sort, fromDate, toDate);
        List<UserFinanceDto> content = Optional.ofNullable(financeRepository.findByFilter(pageFilter))
                .orElseThrow(() -> new NotFoundException("No content found with: %s".formatted(pageFilter)))
                .stream()
                .map(DtoMapper::toDto)
                .toList();

        return PageMapper.toPage(pageFilter, content);
    }
}
