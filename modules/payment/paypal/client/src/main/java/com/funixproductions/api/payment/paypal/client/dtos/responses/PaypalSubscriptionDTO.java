package com.funixproductions.api.payment.paypal.client.dtos.responses;

import com.funixproductions.core.crud.dtos.ApiDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#subscriptions_create">Création d'un sub</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalSubscriptionDTO extends ApiDTO {

    /**
     * Le plan, pour lequel l'abonnement est créé. Doit au moins contenir l'id et le planId du plan pour la création.
     */
    private PaypalPlanDTO plan;

    /**
     * L'id utilisateur de la funixproductions
     */
    private UUID funixProdUserId;

    /**
     * Si l'abonnement est actif ou non. (pause ou pas)
     */
    private Boolean active;

    /**
     * Le nombre de cycles d'abonnement terminés
     */
    private Integer cyclesCompleted;

    /**
     * La date du dernier paiement
     */
    private Date lastPaymentDate;

    /**
     * La date du prochain paiement
     */
    private Date nextPaymentDate;

}
