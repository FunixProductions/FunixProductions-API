package com.funixproductions.api.payment.paypal.service.subscriptions.clients;

import com.funixproductions.api.payment.paypal.service.config.PaypalFeignInterceptor;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalSubscriptionRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalSubscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#subscriptions_create">Create Subscription</a>
 */
@FeignClient(
        name = "PaypalServiceSubscriptionsClient",
        url = "${paypal.paypal-domain}",
        path = "/v1/billing/subscriptions/",
        configuration = PaypalFeignInterceptor.class
)
public interface PaypalServiceSubscriptionsClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalSubscriptionResponse createSubscription(
            @RequestHeader(name = "PayPal-Request-Id") String requestId,
            @RequestBody CreatePaypalSubscriptionRequest request
    );

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalSubscriptionResponse getSubscription(@PathVariable("id") String id);

    @PostMapping(value = "{id}/suspend", produces = MediaType.APPLICATION_JSON_VALUE)
    void pauseSubscription(@PathVariable("id") String id, @RequestBody String reason);

    @PostMapping(value = "{id}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    void activateSubscription(@PathVariable("id") String id);

    @PostMapping(value = "{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    void cancelSubscription(@PathVariable("id") String id, @RequestBody String reason);

}
