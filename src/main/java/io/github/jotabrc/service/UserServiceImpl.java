package io.github.jotabrc.service;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.AddUser;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.util.RoleName;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl() {
        this.userRepository = DependencyInjection.createUserRepository();
        this.roleRepository = DependencyInjection.createRoleRepository();
    }

    @Override
    public String add(AddUser dto) {
        try {
            User user = buildUser(dto);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    private User buildUser(final AddUser dto) throws Exception {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(dto.getPassword(), salt);

        // Throws RoleNotFoundException if Role USER is not found
        Role role = getRoleUser();

        return User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .role(role)
                .isActive(true)
                .hash(hash)
                .salt(encodedSalt)
                .build();
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
