package com.funixproductions.api.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = "com.funixproductions")
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsApp {

    public static void main(final String[] args) {
        SpringApplication.run(FunixProductionsApp.class);
    }

}
