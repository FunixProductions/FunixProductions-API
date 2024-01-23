package com.funixproductions.api.payment.paypal.client.dtos.responses;

import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.client.enums.OrderStatus;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaypalOrderDTO {

    /**
     * The ID of the order.
     */
    private String orderId;

    /**
     * The date and time when the transaction occurred, in <a href="https://tools.ietf.org/html/rfc3339#section-5.6">Internet date and time format</a>.
     */
    private String createTime;

    /**
     * The date and time when the transaction was last updated, in Internet date and time format.
     */
    private String updateTime;

    private Boolean creditCardPayment;

    private OrderStatus status;

    private List<PaymentDTO.PurchaseUnitDTO> purchaseUnitDTOS;

    private VATInformation vatInformation;

    private String urlClientRedirection;

}
