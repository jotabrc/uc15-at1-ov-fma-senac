package io.github.jotabrc.service;

import io.github.jotabrc.dto.PaymentDto;
import io.github.jotabrc.handler.NotFoundException;
import io.github.jotabrc.model.Payment;
import io.github.jotabrc.repository.FinanceRepository;
import io.github.jotabrc.repository.FinancialRepository;
import io.github.jotabrc.repository.util.DQML;
import io.github.jotabrc.security.ApplicationContextHolder;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.EntityCreator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public final class FinancialServiceImpl implements FinancialService {

    private final ApplicationContextHolder applicationContextHolder;
    private final FinanceRepository financeRepository;
    private final FinancialRepository financialRepository;

    public FinancialServiceImpl() {
        this.financialRepository = DependencySelectorImpl.getInstance().select(FinancialRepository.class);
        this.applicationContextHolder = DependencySelectorImpl.getInstance().select(ApplicationContextHolder.class);
        this.financeRepository = DependencySelectorImpl.getInstance().select(FinanceRepository.class);
    }

    @Override
    public void save(final PaymentDto dto) {
        applicationContextHolder.authIsActive();
        String userUuid = applicationContextHolder.getContextDetail();
        Map<String, String> uuidList = financeRepository.findByUserUuid(userUuid);
        Payment payment = EntityCreator.toEntity(dto, uuidList.get("uuid"), uuidList.get("user_finance_uuid"));
        financialRepository.persist(payment, DQML.INSERT);
    }

    @Override
    public void update(final String uuid, final PaymentDto dto) {
        applicationContextHolder.authIsActive();
        String userUuid = financeRepository.getFinancialEntityUserUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User financial information not found with UUID %s".formatted(uuid)));
        applicationContextHolder.hasAuthorization(userUuid);
        Payment payment = findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Payment with UUID %s not found".formatted(uuid)));
        updatePayment(payment, dto);
        financialRepository.persist(payment, DQML.UPDATE);
    }

    @Override
    public void delete(final String uuid) {
        applicationContextHolder.authIsActive();
        String userUuid = financeRepository.getFinancialEntityUserUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User financial information not found with UUID %s".formatted(uuid)));
        applicationContextHolder.hasAuthorization(userUuid);
        Payment payment = findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Payment with UUID %s not found".formatted(uuid)));
        financialRepository.persist(payment, DQML.DELETE);
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
