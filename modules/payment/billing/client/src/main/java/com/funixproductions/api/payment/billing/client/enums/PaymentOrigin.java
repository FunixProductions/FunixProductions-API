package com.funixproductions.api.payment.billing.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentOrigin {
    PACIFISTA("Pacifista", "https://pacifista.fr/cgv"),
    FUNIXGAMING("FunixGaming", "https://funixgaming.fr/cgv"),
    OTHER("", "https://funixproductions.com/cgv");

    private final String name;
    private final String cgv;
}
