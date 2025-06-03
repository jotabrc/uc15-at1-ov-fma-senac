package io.github.jotabrc.util;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.*;

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

    public static FinancialEntity toEntity(final FinancialEntityDto dto, final String userFinanceUuid) {
        return switch (dto) {
            case PaymentDto e -> toEntity(e).setUserFinanceUuid(userFinanceUuid);
            case ReceiptDto e -> toEntity(e).setUserFinanceUuid(userFinanceUuid);
            case RecurringPaymentDto e -> toEntity(e).setUserFinanceUuid(userFinanceUuid);
            case RecurringReceiptDto e -> toEntity(e).setUserFinanceUuid(userFinanceUuid);
            case null, default -> throw new IllegalArgumentException("Entity type not supported");
        };
    }

    public static Payment toEntity(PaymentDto dto) {
        return new Payment(
                UUID.randomUUID().toString(),
                null,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                LocalDateTime.now(),
                0,
                dto.getPayee()
        );
    }

    public static Receipt toEntity(ReceiptDto dto) {
        return new Receipt(
                UUID.randomUUID().toString(),
                null,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                LocalDateTime.now(),
                0,
                dto.getVendor()
        );
    }

    public static RecurringPayment toEntity(RecurringPaymentDto dto) {
        return new RecurringPayment(
                UUID.randomUUID().toString(),
                null,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                LocalDateTime.now(),
                0,
                dto.getRecurringUntil(),
                dto.getPayee()
        );
    }

    public static RecurringReceipt toEntity(RecurringReceiptDto dto) {
        return new RecurringReceipt(
                UUID.randomUUID().toString(),
                null,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                LocalDateTime.now(),
                0,
                dto.getRecurringUntil(),
                dto.getVendor()
        );
    }
}
