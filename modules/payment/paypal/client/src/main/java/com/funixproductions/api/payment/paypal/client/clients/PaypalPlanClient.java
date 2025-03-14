package com.funixproductions.api.payment.paypal.client.clients;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    /**
     * Met à jour un plan.
     * @param id id du plan (id de paypal)
     * @param paypalPlanDTO Dto pour mettre à jour un plan
     */
    @PatchMapping("{id}")
    PaypalPlanDTO updatePlan(@PathVariable String id, @RequestBody @Valid PaypalPlanDTO paypalPlanDTO);

    /**
     * Active un plan, par ID.
     * @param id id du plan (id de paypal)
     */
    @PostMapping("{id}/activate")
    PaypalPlanDTO activatePlan(@PathVariable @Valid @NotBlank String id);

    /**
     * Deactivates a plan, by ID.
     * @param id the plan ID (PayPal ID)
     */
    @PostMapping("{id}/deactivate")
    PaypalPlanDTO deactivatePlan(@PathVariable @Valid @NotBlank String id);

    @PostMapping("{id}/update-price")
    PaypalPlanDTO updatePricePlan(
            @PathVariable String id,
            @RequestBody @Valid @NotNull(message = "Le prix du produit est obligatoire") @Min(value = 1, message = "Le prix du produit doit être supérieur à 1") Double newPrice
    );

    @GetMapping
    PageDTO<PaypalPlanDTO> getAll(
            @RequestParam(value = "page",defaultValue = "0") String page,
            @RequestParam(value = "elemsPerPage",defaultValue = "300") String elemsPerPage,
            @RequestParam(value = "search",defaultValue = "") String search,
            @RequestParam(value = "sort",defaultValue = "") String sort
    );

}
