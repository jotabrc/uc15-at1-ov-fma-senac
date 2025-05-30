package io.github.jotabrc.dto;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * UserDto Data Access Object.
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private String name;
    private String password;
}
