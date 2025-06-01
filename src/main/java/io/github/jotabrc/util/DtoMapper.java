package io.github.jotabrc.util;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.*;

import java.util.List;
import java.util.Optional;

public class DtoMapper {

    private DtoMapper() {}

    public static UserDto toDto(final User user) {
        return UserDto
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserFinanceDto toDto(final UserFinance userFinance) {
        return UserFinanceDto
                .builder()
                .userId(userFinance.getUserId())
                .financialItems(
                        Optional.of(
                                userFinance.getFinancialItems()
                                        .stream()
                                        .map(DtoMapper::toDto)
                                        .toList()
                        ).orElse(List.of())
                )
                .build();
    }

    public static FinancialEntityDto toDto(final FinancialEntity financialEntity) {
        return switch (financialEntity) {
            case Payment e -> toDto(e);
            case Receipt e -> toDto(e);
            case RecurringPayment e -> toDto(e);
            case RecurringReceipt e -> toDto(e);
            case null, default -> throw new IllegalArgumentException("Entity type not supported");
        };
    }

    public static PaymentDto toDto(final Payment e) {
        return new PaymentDto(
                e.getDueDate(),
                e.getAmount(),
                e.getDescription(),
                e.getPayee());
    }

    public static ReceiptDto toDto(final Receipt e) {
        return new ReceiptDto(
                e.getDueDate(),
                e.getAmount(),
                e.getDescription(),
                e.getVendor());
    }

    public static RecurringPaymentDto toDto(final RecurringPayment e) {
        return new RecurringPaymentDto(
                e.getDueDate(),
                e.getAmount(),
                e.getDescription(),
                e.getRecurringUntil(),
                e.getPayee());
    }

    public static RecurringReceiptDto toDto(final RecurringReceipt e) {
        return new RecurringReceiptDto(
                e.getDueDate(),
                e.getAmount(),
                e.getDescription(),
                e.getRecurringUntil(),
                e.getVendor());
    }
}
