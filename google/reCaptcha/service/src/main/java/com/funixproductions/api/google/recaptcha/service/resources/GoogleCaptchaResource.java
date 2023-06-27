package com.funixproductions.api.google.recaptcha.service.resources;

import com.funixproductions.api.google.recaptcha.service.services.GoogleCaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("google/recaptcha")
@RequiredArgsConstructor
public class GoogleCaptchaResource {

    private final GoogleCaptchaService googleCaptchaService;

    @PostMapping("verify")
    public void verify(final HttpServletRequest servletRequest) {
        this.googleCaptchaService.checkCode(servletRequest, servletRequest.getHeader("X-Captcha-Google-Action"));
    }

}
