package com.funixproductions.api.service.google.gmail.resources;

import com.funixproductions.api.service.google.gmail.services.GoogleGmailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/google/gmail")
@RequiredArgsConstructor
public class GoogleGmailResource {

    private final GoogleGmailAuthService googleGmailAuthService;

    @PostMapping("validateClient")
    public void validateClient(@RequestParam String credential, @RequestParam String aud) {
        this.googleGmailAuthService.validateClient(credential, aud);
    }

}
