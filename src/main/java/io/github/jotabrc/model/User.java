package io.github.jotabrc.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * AddUser Data Access Object.
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long version;
}
