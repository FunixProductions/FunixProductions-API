package com.funixproductions.api.payment.paypal.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsPaypalOrderClient",
        url = "${funixproductions.api.payment.paypal.app-domain-url}",
        path = "/encryption"
)
public interface PaypalOrderFeignClient extends PaypalOrderClient {
}
