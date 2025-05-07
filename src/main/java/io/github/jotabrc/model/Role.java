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

    // Role relational ID
    // AUTO_INCREMENT
    // BIGINT
    private long id;
    private String uuid;
    private RoleName name;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long version;
}
