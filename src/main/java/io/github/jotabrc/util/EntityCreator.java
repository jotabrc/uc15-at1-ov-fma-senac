package io.github.jotabrc.util;

import io.github.jotabrc.dto.PaymentDto;
import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.dto.UserRegisterDto;
import io.github.jotabrc.model.Payment;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

public class EntityCreator {

    private EntityCreator() {}

    public static User toEntity(final UserRegisterDto dto) throws NoSuchAlgorithmException {
        PasswordUtil.SaltAndHash saltAndHash = PasswordUtil.getSaltAndHash(dto);
        return User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .isActive(true)
                .hash(saltAndHash.hash())
                .salt(saltAndHash.encodedSalt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Role toEntity(final RoleDto dto) {
        return Role
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .version(0)
                .build();
    }

    public static Payment toEntity(final PaymentDto dto, final String uuid, final String userFinanceUuid) {
        return new Payment(
                uuid,
                userFinanceUuid,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                LocalDateTime.now(),
                0,
                dto.getPayee()
                );
    }
}
