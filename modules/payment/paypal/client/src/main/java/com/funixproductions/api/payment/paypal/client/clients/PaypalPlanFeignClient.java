package com.funixproductions.api.payment.paypal.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsPaypalPlanFeignClient",
        url = "http://payment-paypal",
        path = "/paypal/plans"
)
public interface PaypalPlanFeignClient extends PaypalPlanClient {
}
