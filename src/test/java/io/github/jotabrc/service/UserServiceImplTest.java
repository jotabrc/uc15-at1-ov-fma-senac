package io.github.jotabrc.service;

import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.security.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void add() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.save(any(), any())).thenReturn(null);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role()));
        String uuid = userService.add(UserDto
                .builder()
                .username("username")
                .email("email@email.com")
                .name("John")
                .password("password1234")
                .build());

        assert uuid != null;
    }

    @Test
    public void update() {
        User user = User
                .builder()
                .uuid("uuid")
                .username("sample")
                .email("sample@sample")
                .role(new Role())
                .salt("salt")
                .hash("hash")
                .isActive(true)
                .createdAt(LocalDateTime.of(1992, 2, 18, 0, 0).atZone(ZoneId.of("UTC")))
                .updatedAt(null)
                .version(0)
                .build();
        doNothing().when(applicationContext).checkExpiration();
        when(applicationContext.getUserUuid()).thenReturn(Optional.of("uuid"));
        when(userRepository.findByUuid(any())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.save(any(), any())).thenReturn(null);
        try {
            userService.update(UserDto
                    .builder()
                    .username("username")
                    .email("email@email.com")
                    .name("John")
                    .password("password1234")
                    .build());
            assert user.getUuid().equals("uuid");
            assert user.getUsername().equals("username");
            assert user.getEmail().equals("email@email.com");
            assert user.getName().equals("John");
            assert !user.getSalt().equals("salt");
            assert !user.getHash().equals("hash");
            assert user.getUpdatedAt() != null;
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    public void findByUuid() {
        User user = User
                .builder()
                .uuid("uuid")
                .username("sample")
                .email("sample@sample")
                .role(new Role())
                .salt("salt")
                .hash("hash")
                .isActive(true)
                .createdAt(LocalDateTime.of(1992, 2, 18, 0, 0).atZone(ZoneId.of("UTC")))
                .updatedAt(null)
                .version(0)
                .build();
        doNothing().when(applicationContext).checkExpiration();
        when(applicationContext.getUserUuid()).thenReturn(Optional.of("uuid"));
        when(userRepository.findByUuid(any())).thenReturn(Optional.of(user));
        UserDto dto = userService.findByUuid();
        assert dto.getEmail().equals("sample@sample");
    }
}