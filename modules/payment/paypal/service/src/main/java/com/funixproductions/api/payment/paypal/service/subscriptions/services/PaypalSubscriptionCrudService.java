package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalPlan;
import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalSubscription;
import com.funixproductions.api.payment.paypal.service.subscriptions.mappers.PaypalSubscriptionMapper;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalPlanRepository;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalSubscriptionRepository;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class PaypalSubscriptionCrudService extends ApiService<PaypalSubscriptionDTO, PaypalSubscription, PaypalSubscriptionMapper, PaypalSubscriptionRepository> {

    private final PaypalPlanRepository paypalPlanRepository;

    public PaypalSubscriptionCrudService(
            PaypalSubscriptionRepository repository,
            PaypalPlanRepository paypalPlanRepository,
            PaypalSubscriptionMapper mapper
    ) {
        super(repository, mapper);
        this.paypalPlanRepository = paypalPlanRepository;
    }

    @Transactional
    protected PaypalSubscriptionDTO createFromRequest(final PaypalCreateSubscriptionDTO paypalCreateSubscriptionDTO) {
        return this.create(
                super.getMapper().toDTOFromCreation(paypalCreateSubscriptionDTO)
        );
    }

    @Override
    public void afterMapperCall(@NonNull PaypalSubscriptionDTO dto, @NonNull PaypalSubscription entity) {
        if (dto.getPlan().getId() == null) {
            throw new ApiBadRequestException("Le plan id est obligatoire.");
        }

        entity.setPlan(
                this.getPlan(dto.getPlan().getId().toString())
        );
    }

    @NonNull
    private PaypalPlan getPlan(@NonNull String planId) {
        return paypalPlanRepository.findByUuid(planId).orElseThrow(() -> new ApiNotFoundException("Plan " + planId + " not found"));
    }
}
