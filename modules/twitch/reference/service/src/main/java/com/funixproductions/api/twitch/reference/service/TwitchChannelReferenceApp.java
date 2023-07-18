package com.funixproductions.api.twitch.reference.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.user",
        "com.funixproductions.api.twitch.reference.service",
        "com.funixproductions.api.twitch.auth"
})
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class TwitchChannelReferenceApp {
    public static void main(String[] args) {
        SpringApplication.run(TwitchChannelReferenceApp.class, args);
    }
}
