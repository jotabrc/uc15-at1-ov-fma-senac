package io.github.jotabrc.dto;

import io.github.jotabrc.util.RoleName;
import lombok.*;

/**
 * Role Data Access Object.
 */
@Getter
@Builder
public class RoleDto {

    private final RoleName name;
    private final String description;
}
