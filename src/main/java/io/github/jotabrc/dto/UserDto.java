package io.github.jotabrc.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * UserRegisterDto Data Access Object.
 */
@Getter
@Builder
public class UserDto {

    private final String username;
    private final String email;
    private final String name;
}
