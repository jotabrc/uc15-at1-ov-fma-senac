package io.github.jotabrc.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public final class UserFinanceDto implements Serializable {

    private final String userUuid;
    private final List<FinancialEntityDto> financialItems;

    public UserFinanceDto(
            String userUuid,
            List<FinancialEntityDto> financialItems) {
        this.userUuid = userUuid;
        this.financialItems = financialItems;
    }
}
