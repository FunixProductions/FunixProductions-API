package com.funixproductions.api.payment.paypal.service.subscriptions.clients;

import com.funixproductions.api.payment.paypal.service.config.PaypalFeignInterceptor;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalProductRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <a href="https://developer.paypal.com/docs/api/catalog-products/v1/#products_create">Products</a>
 */
@FeignClient(
        name = "PaypalServiceProductsClient",
        url = "${paypal.paypal-domain}",
        path = "/v1/catalogs/products/",
        configuration = PaypalFeignInterceptor.class
)
public interface PaypalServiceProductsClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalProductResponse createProduct(
            @RequestHeader(name = "PayPal-Request-Id") String requestId,
            @RequestBody CreatePaypalProductRequest request
    );

}
