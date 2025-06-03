package io.github.jotabrc.service;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.handler.NotFoundException;
import io.github.jotabrc.model.*;
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
    public void save(final FinancialEntityDto dto) {
        applicationContextHolder.authIsActive();
        String userUuid = applicationContextHolder.getContextDetail();
        Map<String, String> uuidList = financeRepository.findByUserUuid(userUuid);
        FinancialEntity e = EntityCreator.toEntity(dto, uuidList.get("user_finance_uuid"));
        financialRepository.persist(e, DQML.INSERT);
    }

    @Override
    public void update(final String uuid, final FinancialEntityDto dto) {
        applicationContextHolder.authIsActive();
        String userUuid = financeRepository.getFinancialEntityUserUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User financial information not found with UUID %s".formatted(uuid)));
        applicationContextHolder.hasAuthorization(userUuid);
        FinancialEntity entity = findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Payment with UUID %s not found".formatted(uuid)));
        updatePayment(entity, dto);
        financialRepository.persist(entity, DQML.UPDATE);
    }

    @Override
    public void delete(final String uuid) {
        applicationContextHolder.authIsActive();
        String userUuid = financeRepository.getFinancialEntityUserUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User financial information not found with UUID %s".formatted(uuid)));
        applicationContextHolder.hasAuthorization(userUuid);
        FinancialEntity entity = findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Entity with UUID %s not found".formatted(uuid)));
        financialRepository.persist(entity, DQML.DELETE);
    }

    @Override
    public Optional<FinancialEntity> findByUuid(final String uuid) {
        return financialRepository.findByUuid(uuid);
    }

    private void updatePayment(final FinancialEntity entity, final FinancialEntityDto dto) {
        boolean isWrongType = false;
        switch (entity) {
            case Payment payment -> {
                if (dto instanceof PaymentDto p) payment.setPayee(p.getPayee());
                else isWrongType = true;
            }
            case Receipt receipt -> {
                if (dto instanceof ReceiptDto r) receipt.setVendor(r.getVendor());
                else isWrongType = true;
            }
            case RecurringPayment payment -> {
                if (dto instanceof RecurringPaymentDto p)
                    payment
                            .setPayee(p.getPayee())
                            .setRecurringUntil(p.getRecurringUntil());
                else isWrongType = true;
            }
            case RecurringReceipt receipt -> {
                if (dto instanceof RecurringReceiptDto r)
                    receipt
                            .setVendor(r.getVendor())
                            .setRecurringUntil(r.getRecurringUntil());
                else isWrongType = true;
            }
            case null, default -> isWrongType = true;
        }

        if (isWrongType) throw new IllegalArgumentException("Entity type not supported");

        entity
                .setUpdatedAt(LocalDateTime.now())
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription())
                .setDueDate(dto.getDueDate())
                .setVersion(entity.getVersion() + 1);
    }
}
