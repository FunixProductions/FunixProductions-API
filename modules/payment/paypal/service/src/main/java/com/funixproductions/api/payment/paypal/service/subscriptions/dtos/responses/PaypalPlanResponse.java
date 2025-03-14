package com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#plans_create">Create plan</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPlanResponse {

    private String id;

}
