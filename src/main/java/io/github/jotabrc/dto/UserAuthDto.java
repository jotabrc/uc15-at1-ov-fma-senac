package io.github.jotabrc.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAuthDto {

    private final String email;
    private final String password;
}
