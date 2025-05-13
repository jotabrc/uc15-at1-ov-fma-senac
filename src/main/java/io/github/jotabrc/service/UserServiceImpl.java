package io.github.jotabrc.service;

import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.security.ApplicationContext;
import io.github.jotabrc.util.RoleName;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ApplicationContext applicationContext;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ApplicationContext applicationContext) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public String add(UserDto dto) {
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
    public void update(UserDto dto) throws Exception {
        applicationContext.checkExpiration();
        String uuid = getContextUuid();
        User user = getUserWithUuid(uuid);
        checkAvailability(dto, user);

        SaltAndHash saltAndHash = getSaltAndHash(dto);
        user
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setName(dto.getName())
                .setSalt(saltAndHash.encodedSalt())
                .setHash(saltAndHash.hash())
                .setUpdatedAt(LocalDateTime.now().atZone(ZoneId.of("UTC")))
                .setVersion(user.getVersion() + 1);

        userRepository.save(user, DQML.UPDATE);
    }

    @Override
    public UserDto findByUuid(String uuid) {
        User user = getUserWithUuid(uuid);
        return toDto(user);
    }

    private void checkAvailability(final UserDto dto, final User user) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (user == null || !user.getUsername().equals(dto.getUsername())) checkUsernameAvailability(dto.getUsername(), joiner);
        if (user == null || !user.getEmail().equals(dto.getEmail())) checkEmailAvailability(dto.getEmail(), joiner);
        if (joiner.length() > 2) throw new RuntimeException(joiner.toString());
    }

    private void checkUsernameAvailability(final String username, final StringJoiner joiner) {
        if (userRepository.existsByUsername(username)) joiner.add("Username %s unavailable".formatted(username));
    }

    private void checkEmailAvailability(final String email, final StringJoiner joiner) {
        if (userRepository.existsByEmail(email)) joiner.add("Email %s unavailable".formatted(email));
    }

    private User buildUser(final UserDto dto) throws Exception {
        SaltAndHash saltAndHash = getSaltAndHash(dto);
        Role role = getRoleUser();

        return User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .role(role)
                .isActive(true)
                .hash(saltAndHash.hash())
                .salt(saltAndHash.encodedSalt())
                .createdAt(LocalDateTime.now().atZone(ZoneId.of("UTC")))
                .build();
    }

    private SaltAndHash getSaltAndHash(final UserDto dto) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(dto.getPassword(), salt);
        return new SaltAndHash(encodedSalt, hash);
    }

    private record SaltAndHash(String encodedSalt, String hash) {
    }

    private Role getRoleUser() throws Exception {
        return roleRepository.findByName(RoleName.USER.getName())
                .orElseThrow(() -> new Exception("Role USER not found"));
    }

    private String getEncodedSalt(final byte[] salt) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(salt);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    private String getHash(final String password, final byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    private String getHash(final String password, final String salt) throws NoSuchAlgorithmException {
        byte[] saltByte = Base64.getDecoder().decode(salt);
        return getHash(password, saltByte);
    }

    private String getContextUuid() {
        return applicationContext.getUserUuid().orElseThrow(() -> new RuntimeException("Access denied"));
    }

    private User getUserWithUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("User with UUID %s not found".formatted(uuid)));
    }

    private UserDto toDto(final User user) {
        Function<User, UserDto> fn = (u) ->
                UserDto
                .builder()
                .username(u.getUsername())
                .email(u.getEmail())
                .name(u.getName())
                .password(null)
                .build();
        return fn.apply(user);
    }
}
