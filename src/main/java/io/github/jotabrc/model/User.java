package io.github.jotabrc.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

/**
 * UserDto Data Access Object.
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    private String uuid;
    private String username;
    private String email;
    private String name;
    private Role role;
    private String salt;
    private String hash;
    private boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private long version;
}
