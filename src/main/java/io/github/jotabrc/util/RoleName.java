package io.github.jotabrc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Role names for Role entity standardization.
 */
@Getter
@AllArgsConstructor
public enum RoleName {

    USER("USER"),
    ADMIN("ADMIN");

    private final String name;

    public static RoleName getRole(final String name) {
        for (RoleName role : RoleName.values()) {
            if (role.getName().equals(name)) return role;
        }
        return null;
    }
}
