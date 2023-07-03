package com.funixproductions.api.twitch.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.user",
        "com.funixproductions.api.twitch.auth.service",
        "com.funixproductions.api.encryption"
})
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsTwitchAuthApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsTwitchAuthApp.class, args);
    }
}
