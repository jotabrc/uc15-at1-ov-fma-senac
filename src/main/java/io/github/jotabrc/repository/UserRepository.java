package io.github.jotabrc.repository;

import io.github.jotabrc.model.User;

public interface UserRepository {

    String save(final User user);
    boolean existsByUsername(final String username);
    boolean existsByEmail(final String email);
}
