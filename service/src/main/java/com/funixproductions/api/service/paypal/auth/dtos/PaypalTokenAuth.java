package com.funixproductions.api.service.paypal.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PaypalTokenAuth {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "app_id")
    private String appId;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    private final Instant generatedAt = Instant.now();

    public boolean isUsable() {
        if (expiresIn == null) {
            return false;
        } else {
            final Instant now = Instant.now().plusSeconds(5);
            return generatedAt.plusSeconds(expiresIn).isAfter(now);
        }
    }

}
