package com.funixproductions.api.payment.paypal.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaypalOrderDTO extends ApiDTO {

    private String orderId;

}
