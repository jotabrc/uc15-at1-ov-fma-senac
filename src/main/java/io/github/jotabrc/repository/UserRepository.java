package io.github.jotabrc.repository;

import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.util.DQML;

import java.util.Optional;

public interface UserRepository {

    String save(final User user, final DQML dqml);
    Optional<User> findByUuid(final String uuid);
    Optional<User> findByEmail(final String email);
    boolean existsByUsername(final String username);
    boolean existsByEmail(final String email);
}
