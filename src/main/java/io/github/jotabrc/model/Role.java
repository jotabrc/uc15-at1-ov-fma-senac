package io.github.jotabrc.model;

import io.github.jotabrc.util.RoleName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Role Data Access Object.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private String uuid;
    private RoleName name;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long version;
}
