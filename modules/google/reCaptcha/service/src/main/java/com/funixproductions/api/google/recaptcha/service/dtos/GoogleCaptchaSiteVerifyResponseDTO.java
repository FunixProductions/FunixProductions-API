package com.funixproductions.api.google.recaptcha.service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleCaptchaSiteVerifyResponseDTO {
    public static final float MINIMAL_THRESHOLD = 0.65f;

    private boolean success;

    private String hostname;

    private float score;

    private String action;

    @JsonProperty("error-codes")
    private List<String> errorCodes;

    public boolean isValidCaptcha(final String actionCodeToCompare, final float thresholdMinimal) {
        return this.isSuccess() &&
                this.getAction().equals(actionCodeToCompare) &&
                this.getScore() > thresholdMinimal;
    }

    public boolean isValidCaptcha(final String actionCodeToCompare) {
        return this.isSuccess() &&
                this.getAction().equals(actionCodeToCompare) &&
                this.getScore() > MINIMAL_THRESHOLD;
    }
}
