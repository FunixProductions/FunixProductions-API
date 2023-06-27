package com.funixproductions.api.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = "com.funixproductions")
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsUserApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsUserApp.class, args);
    }
}
