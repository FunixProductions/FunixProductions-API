package com.funixproductions.api.client.google.recaptcha.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "GoogleCaptchaClient",
        url = "${funixproductions.api.app-domain-url}",
        path = "/google/recaptcha/"
)
public interface GoogleCaptchaClient {

    @PostMapping("verify")
    void verify(@RequestHeader("X-Captcha-Google-Code") String code,
                @RequestHeader("X-Captcha-Google-Action") String action,
                @RequestHeader("X-FORWARDED-FOR") String ip);

}
