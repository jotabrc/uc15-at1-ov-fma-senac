package io.github.jotabrc.dto;

import io.github.jotabrc.util.RoleName;
import lombok.*;

/**
 * Role Data Access Object.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRole {

    private RoleName name;
    private String description;
}
