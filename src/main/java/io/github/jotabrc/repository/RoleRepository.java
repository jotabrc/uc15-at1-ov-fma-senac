package io.github.jotabrc.repository;

import io.github.jotabrc.model.Role;

public interface RoleRepository {

    Role get(final String name);
}
