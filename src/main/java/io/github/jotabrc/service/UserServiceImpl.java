package io.github.jotabrc.service;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.util.RoleName;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.StringJoiner;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl() {
        this.userRepository = DependencyInjection.createUserRepository();
        this.roleRepository = DependencyInjection.createRoleRepository();
    }

    @Override
    public String add(UserDto dto) {
        try {
            checkAvailability(dto);

            User user = buildUser(dto);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    private void checkAvailability(final UserDto dto) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (userRepository.existsByEmail(dto.getEmail())) joiner.add("Email %s unavailable".formatted(dto.getEmail()));
        if (userRepository.existsByUsername(dto.getUsername())) joiner.add("Username %s unavailable".formatted(dto.getUsername()));
        if (joiner.length() > 0) throw new RuntimeException(joiner.toString());
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
}
