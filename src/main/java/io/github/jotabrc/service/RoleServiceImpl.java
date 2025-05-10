package io.github.jotabrc.service;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.AddRole;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.repository.RoleRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl() {
        this.roleRepository = DependencyInjection.createRoleRepository();
    }

    @Override
    public String add(AddRole dto) {
        Role role = buildRole(dto);
        return roleRepository.save(role);
    }

    private Role buildRole(final AddRole dto) {
        return Role
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .version(0)
                .build();
    }
}
