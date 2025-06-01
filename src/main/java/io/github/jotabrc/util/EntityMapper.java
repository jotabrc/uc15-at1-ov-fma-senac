package io.github.jotabrc.util;

import io.github.jotabrc.dto.FinancialEntityDto;
import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.dto.UserFinanceDto;
import io.github.jotabrc.dto.UserRegisterDto;
import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.model.UserFinance;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntityMapper {

    private EntityMapper() {}

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
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Role toEntity(final RoleDto dto) {
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

    public static UserFinance toEntity(final UserFinanceDto dto) {
        return UserFinance
                .builder()
                .userId(dto.getUserId())
                .financialItems(Optional
                        .ofNullable(dto.getFinancialItems())
                        .orElse(List.of())
                        .stream()
                        .map(EntityMapper::toEntity)
                        .toList())
                .build();
    }

    public static FinancialEntity toEntity(final FinancialEntityDto dto) {
        return FinancialEntity
                .builder()
                .dueDate(dto.getDueDate())
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
