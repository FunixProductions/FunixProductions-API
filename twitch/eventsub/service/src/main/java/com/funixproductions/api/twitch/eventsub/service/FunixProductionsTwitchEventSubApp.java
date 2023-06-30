package com.funixproductions.api.twitch.eventsub.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.funixproductions")
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsTwitchEventSubApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsTwitchEventSubApp.class, args);
    }
}