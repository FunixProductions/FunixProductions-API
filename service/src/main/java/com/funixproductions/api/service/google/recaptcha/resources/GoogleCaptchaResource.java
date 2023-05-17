package com.funixproductions.api.service.google.recaptcha.resources;

import com.funixproductions.api.service.google.recaptcha.services.GoogleCaptchaService;
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
