package com.funixproductions.api.google.recaptcha.client.clients;

import com.funixproductions.api.google.recaptcha.client.dtos.GoogleCaptchaSiteVerifyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "GoogleRecaptchaClient",
        url = "${funixproductions.api.google.recaptcha.app-domain-url}",
        path = "/google/recaptcha"
)
public interface GoogleRecaptchaClient {

    @PostMapping("verify")
    GoogleCaptchaSiteVerifyResponseDTO verify(@RequestHeader("X-Captcha-Google-Action") String action,
                                              @RequestHeader("X-Captcha-Google-Code") String googleCode,
                                              @RequestHeader("X-FORWARDED-FOR") String ip);

}
