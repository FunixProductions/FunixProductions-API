package com.funixproductions.api.payment.paypal.client.clients;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

/**
 * Client pour gérer les plans d'abonnements Paypal.
 */
public interface PaypalPlanClient {

    /**
     * Crée un abonnement mensuel sur Paypal.
     * @param paypalPlanDTO Dto pour créer un abonnement mensuel sur Paypal
     * @return le plan créé (201 Http)
     */
    @PostMapping
    PaypalPlanDTO create(@RequestBody @Valid PaypalPlanDTO paypalPlanDTO);

    /**
     * Récupère un plan par son id.
     * @param id id du plan (id de paypal)
     * @return le plan (200 Http)
     */
    @GetMapping("{id}")
    PaypalPlanDTO getPlanById(@PathVariable @Valid @NotBlank String id);

    @GetMapping
    PageDTO<PaypalPlanDTO> getAll(
            @RequestParam(value = "page",defaultValue = "0") String page,
            @RequestParam(value = "elemsPerPage",defaultValue = "300") String elemsPerPage,
            @RequestParam(value = "search",defaultValue = "") String search,
            @RequestParam(value = "sort",defaultValue = "") String sort
    );

}
