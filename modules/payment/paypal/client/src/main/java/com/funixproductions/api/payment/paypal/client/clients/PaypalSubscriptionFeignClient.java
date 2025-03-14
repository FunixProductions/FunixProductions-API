package com.funixproductions.api.payment.paypal.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsPaypalSubscriptionFeignClient",
        url = "http://payment-paypal",
        path = "/paypal/subscriptions"
)
public interface PaypalSubscriptionFeignClient extends PaypalSubscriptionClient {
}
