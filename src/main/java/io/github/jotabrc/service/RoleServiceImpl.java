package io.github.jotabrc.service;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.repository.RoleRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl() {
        this.roleRepository = DependencyInjection.createRoleRepository();
    }

    @Override
    public String add(RoleDto dto) {
        if (roleRepository.existsByName(dto.getName().name()))
            throw new RuntimeException("Role with name %s already exists".formatted(dto.getName()));
        Role role = buildRole(dto);
        return roleRepository.save(role);
    }

    private Role buildRole(final RoleDto dto) {
        return Role
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .createdAt(LocalDateTime.now().atZone(ZoneId.of("UTC")))
                .version(0)
                .build();
    }
}
