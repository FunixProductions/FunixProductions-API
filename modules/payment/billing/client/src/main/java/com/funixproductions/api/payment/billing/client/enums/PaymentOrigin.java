package com.funixproductions.api.payment.billing.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentOrigin {
    PACIFISTA("https://pacifista.fr/cgv"),
    FUNIXGAMING("https://funixgaming.fr/cgv"),
    OTHER("https://funixproductions.com/cgv");

    private final String cgv;
}
