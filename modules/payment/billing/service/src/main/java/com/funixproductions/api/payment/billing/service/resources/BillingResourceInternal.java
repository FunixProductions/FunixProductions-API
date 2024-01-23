package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.payment.billing.service.services.BillingCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kubeinternal/billing")
public class BillingResourceInternal extends BillingResource {
    public BillingResourceInternal(BillingCrudService service) {
        super(service);
    }
}
