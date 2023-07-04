package com.funixproductions.api.twitch.eventsub.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.user",
        "com.funixproductions.api.twitch.reference",
        "com.funixproductions.api.twitch.auth",
        "com.funixproductions.api.twitch.eventsub.service.clients"
})
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsTwitchEventSubApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsTwitchEventSubApp.class, args);
    }
}
