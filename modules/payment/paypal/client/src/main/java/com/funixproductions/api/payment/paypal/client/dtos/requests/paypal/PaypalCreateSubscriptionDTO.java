package com.funixproductions.api.payment.paypal.client.dtos.requests.paypal;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalCreateSubscriptionDTO {

    @NotNull(message = "Le plan est obligatoire")
    private PaypalPlanDTO plan;

    @NotNull(message = "L'utilisateur id est obligatoire")
    private UUID funixProdUserId;

    /**
     * LA TVA, null si pas de TVA
     */
    @Nullable
    private VATInformation vatInformation;

    @NotBlank(message = "L'URL d'annulation est obligatoire")
    private String cancelUrl;

    @NotBlank(message = "L'URL de retour valide est obligatoire")
    private String returnUrl;

    @NotBlank(message = "La marque est obligatoire")
    private String brandName;

}
