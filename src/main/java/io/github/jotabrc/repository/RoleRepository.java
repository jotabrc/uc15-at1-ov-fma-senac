package io.github.jotabrc.repository;

import io.github.jotabrc.model.Role;

import java.util.Optional;

public interface RoleRepository {

    String save(final Role role);
    Optional<Role> findByName(final String name);
    Optional<Role> findByUuid(final String uuid);
    boolean existsByName(final String name);
}
