package com.funixproductions.api.accounting.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.user",
        "com.funixproductions.api.google",
        "com.funixproductions.api.payment.billing"
})
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsAccountingApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsAccountingApp.class, args);
    }
}