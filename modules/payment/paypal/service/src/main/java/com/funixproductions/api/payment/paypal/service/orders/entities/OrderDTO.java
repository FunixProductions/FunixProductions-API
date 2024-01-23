package com.funixproductions.api.payment.paypal.service.orders.entities;

import com.funixproductions.core.crud.dtos.ApiDTO;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO extends ApiDTO {

    private String orderId;

    private String userId;

    private String userEmail;

    private String username;

    private String originRequest;

    private Boolean cardPayment;

    private Boolean paid;

    private VATInformation vatInformation;

}
