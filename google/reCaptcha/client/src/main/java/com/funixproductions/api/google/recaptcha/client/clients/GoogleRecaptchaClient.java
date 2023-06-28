package com.funixproductions.api.google.recaptcha.client.clients;

import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
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
    void verify(@RequestHeader(GoogleRecaptchaHandler.GOOGLE_ACTION_HEADER) String action,
                                              @RequestHeader(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER) String googleCode,
                                              @RequestHeader("X-FORWARDED-FOR") String ip);

}
