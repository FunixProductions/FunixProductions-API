package com.funixproductions.api.payment.billing.service.services;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.api.payment.billing.service.mappers.BillingMapper;
import com.funixproductions.api.payment.billing.service.repositories.BillingRepository;
import com.funixproductions.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class BillingCrudService extends ApiService<BillingDTO, Billing, BillingMapper, BillingRepository> {
    public BillingCrudService(BillingRepository repository, BillingMapper mapper) {
        super(repository, mapper);
    }
}
