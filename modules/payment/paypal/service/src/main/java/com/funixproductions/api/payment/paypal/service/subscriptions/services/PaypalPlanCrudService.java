package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalPlan;
import com.funixproductions.api.payment.paypal.service.subscriptions.mappers.PaypalPlanMapper;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalPlanRepository;
import com.funixproductions.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class PaypalPlanCrudService extends ApiService<PaypalPlanDTO, PaypalPlan, PaypalPlanMapper, PaypalPlanRepository> {

    public PaypalPlanCrudService(PaypalPlanRepository repository, PaypalPlanMapper mapper) {
        super(repository, mapper);
    }

}
