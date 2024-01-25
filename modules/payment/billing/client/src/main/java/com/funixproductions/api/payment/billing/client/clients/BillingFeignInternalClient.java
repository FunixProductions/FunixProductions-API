package com.funixproductions.api.payment.billing.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsBillingClient",
        url = "${funixproductions.api.payment.billing.app-domain-url}",
        path = "/kubeinternal/billing"
)
public interface BillingFeignInternalClient extends BillingInternalClient {
}
