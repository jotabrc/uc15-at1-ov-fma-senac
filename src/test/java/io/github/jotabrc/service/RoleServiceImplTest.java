package io.github.jotabrc.service;

import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.util.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add() {
        when(roleRepository.save(any())).thenReturn("uuid");
        when(roleRepository.existsByName(any())).thenReturn(false);
        String uuid = roleService.add(new RoleDto(RoleName.USER, "User"));
        assert uuid.equals("uuid");
    }
}