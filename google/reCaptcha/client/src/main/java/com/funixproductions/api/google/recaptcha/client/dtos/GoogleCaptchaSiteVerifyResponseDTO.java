package com.funixproductions.api.google.recaptcha.client.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCaptchaSiteVerifyResponseDTO {
    public static final float MINIMAL_THRESHOLD = 0.65f;

    private boolean success;

    private String hostname;

    private float score;

    private String action;

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
