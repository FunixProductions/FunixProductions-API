package com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <a href="https://developer.paypal.com/docs/api/catalog-products/v1/#products_create">Create product</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalProductResponse {

    private String id;

}
