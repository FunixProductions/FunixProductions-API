package com.funixproductions.api.payment.billing.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsBillingClient",
        url = "http://payment-billing",
        path = "/kubeinternal/billing"
)
public interface BillingFeignInternalClient extends BillingClient {
}
