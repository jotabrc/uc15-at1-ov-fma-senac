package io.github.jotabrc.service;

import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.EntityCreator;

public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl() {
        this.roleRepository = DependencySelectorImpl.getInstance().select(RoleRepository.class);
    }

    @Override
    public String add(RoleDto dto) {
        existsByName(dto);
        Role role = buildRole(dto);
        return roleRepository.save(role);
    }

    private void existsByName(RoleDto dto) {
        if (roleRepository.existsByName(dto.getName().name()))
            throw new RuntimeException("Role with name %s already exists".formatted(dto.getName()));
    }

    private Role buildRole(final RoleDto dto) {
        return EntityCreator.toEntity(dto);
    }
}
