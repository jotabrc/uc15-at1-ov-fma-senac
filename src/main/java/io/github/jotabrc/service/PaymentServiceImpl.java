package io.github.jotabrc.service;

import io.github.jotabrc.dto.PaymentDto;
import io.github.jotabrc.handler.NotFoundException;
import io.github.jotabrc.model.FinancialEntity;
import io.github.jotabrc.model.Payment;
import io.github.jotabrc.repository.FinanceRepository;
import io.github.jotabrc.security.ApplicationContextHolder;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.EntityCreator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public final class PaymentServiceImpl implements PaymentService {

    private final ApplicationContextHolder applicationContextHolder;
    private final FinanceRepository financeRepository;

    public PaymentServiceImpl() {
        this.applicationContextHolder = DependencySelectorImpl.getInstance().select(ApplicationContextHolder.class);
        this.financeRepository = DependencySelectorImpl.getInstance().select(FinanceRepository.class);
    }

    @Override
    public void save(final PaymentDto dto) {
        applicationContextHolder.authIsActive();
        String userUuid = applicationContextHolder.getContextDetail();
        Map<String, String> uuidList = financeRepository.findByUserUuid(userUuid);
        FinancialEntity payment = EntityCreator.toEntity(dto, uuidList.get("uuid"), uuidList.get("user_finance_uuid"));
        // call repository to persist
    }

    @Override
    public void update(final String uuid, final PaymentDto dto) {
        applicationContextHolder.authIsActive();
        String userUuid = applicationContextHolder.getContextDetail();
        Payment payment = findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Payment with UUID %s not found".formatted(uuid)));
        updatePayment(payment, dto);
        // call repository to update
    }

    @Override
    public void delete(final String uuid) {
        applicationContextHolder.authIsActive();
        String userUuid = financeRepository.getUserUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User financial information not found with UUID %s".formatted(uuid)));
        applicationContextHolder.hasAuthorization(userUuid);
        // call repository to delete
    }

    @Override
    public Optional<Payment> findByUuid(String uuid) {
        return Optional.empty();
    }

    public void updatePayment(Payment payment, PaymentDto dto) {
        payment
                .setUpdatedAt(LocalDateTime.now())
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription())
                .setDueDate(dto.getDueDate())
                .setVersion(payment.getVersion() + 1);
    }
}
