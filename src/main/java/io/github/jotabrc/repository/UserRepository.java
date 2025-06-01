package io.github.jotabrc.repository;

import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.util.DQML;

import java.util.Optional;

public interface UserRepository {

    String save(User user, DQML dqml);
    Optional<User> findById(long id);
    Optional<User> findByUuid(String uuid);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
