package io.github.jotabrc.util;

import io.github.jotabrc.dto.Page;
import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.dto.UserFinanceDto;
import io.github.jotabrc.model.UserFinance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PageMapper {

    public static <T> Page<T> toPage(final int pageNumber,
                                     final int pageSize,
                                     final String sort,
                                     final List<T> content) {
        return new Page<T>(pageNumber, pageSize, sort, content);
    }

    public static <T> Page<T> toPage(final PageFilter pageFilter, final List<T> content) {
        return new Page<T>(
                pageFilter.pageNumber(),
                pageFilter.pageSize(),
                pageFilter.sort(),
                content);
    }

    public static PageFilter toPageFilter(final String userUuid,
                                          final int pageNumber,
                                          final int pageSize,
                                          final String sort,
                                          final LocalDate fromDate,
                                          final LocalDate toDate) {
        return new PageFilter(userUuid, fromDate, toDate, pageNumber, pageSize, sort);
    }

    public static <T> Page<UserFinanceDto> toPage(Page<T> page) {
        List<UserFinanceDto> content = new ArrayList<>();
        if (page.getContent().getFirst() instanceof UserFinance) {
            content.addAll(
                    page.getContent()
                            .stream()
                            .map(e -> (UserFinance) e)
                            .map(DtoMapper::toDto)
                            .toList()
            );
        }

        return new Page<>(page.getPageNumber(), page.getPageSize(), page.getSort(), content);
    }
}
