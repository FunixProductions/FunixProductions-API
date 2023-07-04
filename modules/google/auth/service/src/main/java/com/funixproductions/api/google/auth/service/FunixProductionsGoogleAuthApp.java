package com.funixproductions.api.google.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.funixproductions.api.user")
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsGoogleAuthApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsGoogleAuthApp.class, args);
    }
}
