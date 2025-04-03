package com.funixproductions.api.payment.paypal.client.clients;

import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

/**
 * Client pour gérer les abonnements des utilisateurs sur Paypal.
 */
public interface PaypalSubscriptionClient {

    @PostMapping
    PaypalSubscriptionDTO subscribe(@RequestBody @Valid PaypalCreateSubscriptionDTO request);

    @GetMapping("{id}")
    PaypalSubscriptionDTO getSubscriptionById(@PathVariable @Valid @NotBlank String id);

    @PostMapping("{id}/pause")
    PaypalSubscriptionDTO pauseSubscription(@PathVariable @Valid @NotBlank String id);

    @PostMapping("{id}/activate")
    PaypalSubscriptionDTO activateSubscription(@PathVariable @Valid @NotBlank String id);

    @PostMapping("{id}/cancel")
    void cancelSubscription(@PathVariable @Valid @NotBlank String id);

    @GetMapping
    PageDTO<PaypalSubscriptionDTO> getAll(
            @RequestParam(value = "page",defaultValue = "0") String page,
            @RequestParam(value = "elemsPerPage",defaultValue = "300") String elemsPerPage,
            @RequestParam(value = "search",defaultValue = "") String search,
            @RequestParam(value = "sort",defaultValue = "") String sort
    );

}
