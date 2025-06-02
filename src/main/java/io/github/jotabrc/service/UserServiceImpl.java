package io.github.jotabrc.service;

import io.github.jotabrc.dto.UserAuthDto;
import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.dto.UserRegisterDto;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.security.ApplicationContextHolder;
import io.github.jotabrc.util.*;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ApplicationContextHolder applicationContextHolder;

    public UserServiceImpl() {
        this.userRepository = DependencySelectorImpl.getInstance().select(UserRepository.class);
        this.roleRepository = DependencySelectorImpl.getInstance().select(RoleRepository.class);
        this.applicationContextHolder =  DependencySelectorImpl.getInstance().select(ApplicationContextHolder.class);;
    }

    @Override
    public String add(UserRegisterDto dto) {
        try {
            checkAvailability(dto, null);
            User user = buildUser(dto);
            userRepository.save(user, DQML.INSERT);
            return user.getUuid();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UserRegisterDto dto) throws Exception {
        try {
            applicationContextHolder.authIsActive();
            String userUuid = applicationContextHolder.getContextDetail();
            User user = getUserWithUuid(userUuid);
            checkAvailability(dto, user);

            PasswordUtil.SaltAndHash saltAndHash = PasswordUtil.getSaltAndHash(dto);
            updateUser(dto, user, saltAndHash);

            userRepository.save(user, DQML.UPDATE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void auth(UserAuthDto dto) throws Exception {
        User user = getUserWithEmail(dto.getEmail());
        applicationContextHolder.auth(dto.getPassword(), user.getSalt(), user.getHash());
        applicationContextHolder.setContext(user.getUuid());
    }

    @Override
    public UserDto findByUuid() {
        applicationContextHolder.authIsActive();
        String userUuid = applicationContextHolder.getContextDetail();
        User user = getUserWithUuid(userUuid);
        return DtoMapper.toDto(user);
    }

    private void checkAvailability(final UserRegisterDto dto, final User user) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> checkUsernameAvailability(dto.getUsername(), user))
                .exceptionally(ex -> {
                    log.error("Error checking username availability: {}", ex.getMessage());
                    return "";
                }).thenCombine(
                        CompletableFuture
                                .supplyAsync(() -> checkEmailAvailability(dto.getEmail(), user))
                                .exceptionally(ex -> {
                                    log.error("Error checking email availability: {}", ex.getMessage());
                                    return "";
                                }), (m1, m2) -> Stream.of(m1, m2)
                                .filter(s -> !s.isEmpty()).collect(Collectors.toList())
                ).thenAccept(messages -> {
                    if (!messages.isEmpty()) throw new RuntimeException("[" + String.join(",", messages) + "]");
                });
        future.join();
    }

    private String checkUsernameAvailability(final String username, final User user) {
        if (user == null || !user.getUsername().equals(username))
            if (userRepository.existsByUsername(username)) return "Username %s unavailable".formatted(username);
        return "";
    }

    private String checkEmailAvailability(final String email, final User user) {
        if (user == null || !user.getEmail().equals(email))
            if (userRepository.existsByEmail(email)) return "Email %s unavailable".formatted(email);
        return "";
    }

    private User buildUser(final UserRegisterDto dto) throws Exception {
        Role role = getRoleUser();
        User user = EntityCreator.toEntity(dto);
        user.setRole(role);
        return user;
    }

    private Role getRoleUser() throws Exception {
        return roleRepository.findByName(RoleName.USER.getName())
                .orElseThrow(() -> new Exception("Role USER not found"));
    }

    private User getUserWithUuid(final String userUuid) {
        return userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User with UUID %s not found".formatted(userUuid)));
    }

    private User getUserWithEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with EMAIL %s not found".formatted(email)));
    }

    private static void updateUser(UserRegisterDto dto, User user, PasswordUtil.SaltAndHash saltAndHash) {
        user
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setName(dto.getName())
                .setSalt(saltAndHash.encodedSalt())
                .setHash(saltAndHash.hash())
                .setUpdatedAt(LocalDateTime.now())
                .setVersion(user.getVersion() + 1);
    }
}
