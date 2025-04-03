package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalPlan;
import com.funixproductions.api.payment.paypal.service.subscriptions.mappers.PaypalPlanMapper;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalPlanRepository;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class PaypalPlanCrudService extends ApiService<PaypalPlanDTO, PaypalPlan, PaypalPlanMapper, PaypalPlanRepository> {

    public PaypalPlanCrudService(PaypalPlanRepository repository, PaypalPlanMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public void beforeSavingEntity(@NonNull Iterable<PaypalPlan> entity) {
        for (PaypalPlan paypalPlan : entity) {
            if (paypalPlan.getId() == null && super.getRepository().existsByNameAndProjectName(paypalPlan.getName(), paypalPlan.getProjectName())) {
                throw new ApiBadRequestException("Le plan existe déjà pour ce projet.");
            }
        }
    }
}
