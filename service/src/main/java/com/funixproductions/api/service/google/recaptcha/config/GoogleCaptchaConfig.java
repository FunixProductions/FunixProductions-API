package com.funixproductions.api.service.google.recaptcha.config;

public interface GoogleCaptchaConfig {
    String getSite();
    String getSecret();
    Float getThreshold();
    boolean isDisabled();
}
