package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.payment.billing.client.clients.BillingClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.services.BillingCrudService;
import com.funixproductions.core.crud.resources.ApiResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/billing")
public class BillingResource extends ApiResource<BillingDTO, BillingCrudService> implements BillingClient {
    public BillingResource(BillingCrudService service) {
        super(service);
    }
}
