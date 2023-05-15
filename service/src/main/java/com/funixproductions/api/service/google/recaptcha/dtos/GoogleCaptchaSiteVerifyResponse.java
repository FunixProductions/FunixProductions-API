package com.funixproductions.api.service.google.recaptcha.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCaptchaSiteVerifyResponse {
    private boolean success;

    private String hostname;

    private float score;

    private String action;
}
