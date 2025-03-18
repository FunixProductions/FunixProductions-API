package com.funixproductions.api.payment.paypal.client.dtos.responses;

import com.funixproductions.core.crud.dtos.ApiDTO;
import lombok.*;
import org.jetbrains.annotations.Nullable;

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
    @NonNull
    private PaypalPlanDTO plan;

    /**
     * L'id de l'abonnement par PayPal
     */
    private String subscriptionId;

    /**
     * L'id utilisateur de la funixproductions
     */
    @NonNull
    private UUID funixProdUserId;

    /**
     * Si l'abonnement est actif ou non. (pause ou pas)
     */
    @NonNull
    private Boolean active;

    /**
     * Le nombre de cycles d'abonnement terminés
     */
    @NonNull
    private Integer cyclesCompleted;

    /**
     * La date du dernier paiement
     */
    @Nullable
    private Date lastPaymentDate;

    /**
     * La date du prochain paiement
     */
    @Nullable
    private Date nextPaymentDate;

    /**
     * Le lien pour approuver l'abonnement
     */
    @Nullable
    private String approveLink;

}
