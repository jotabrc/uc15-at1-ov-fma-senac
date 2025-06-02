package io.github.jotabrc.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public final class UserFinance {

    private String uuid;
    private String userUuid;
    private List<FinancialEntity> financialItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long version;
}
