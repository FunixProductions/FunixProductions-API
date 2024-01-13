package com.funixproductions.api.payment.paypal.client.dtos.requests.paypal;

import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalPaymentDTO extends PaymentDTO {

    @NotBlank(message = "Le nom de la marque est obligatoire")
    private String brandName;

}
