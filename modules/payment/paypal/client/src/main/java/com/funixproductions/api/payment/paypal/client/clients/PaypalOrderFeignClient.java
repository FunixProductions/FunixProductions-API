package com.funixproductions.api.payment.paypal.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsPaypalOrderClient",
        url = "${funixproductions.api.payment.paypal.app-domain-url}",
        path = "/paypal/order"
)
public interface PaypalOrderFeignClient extends PaypalOrderClient {
}
