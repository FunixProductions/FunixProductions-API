package com.funixproductions.api.google.recaptcha.service;

import com.funixproductions.api.google.recaptcha.service.clients.GoogleRecaptchaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = GoogleRecaptchaClient.class)
@SpringBootApplication(scanBasePackages = {
        "com.funixproductions.core",
        "com.funixproductions.api.core",
        "com.funixproductions.api.google.recaptcha.service"
})
public class FunixProductionsGoogleRecaptchaApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsGoogleRecaptchaApp.class, args);
    }
}
