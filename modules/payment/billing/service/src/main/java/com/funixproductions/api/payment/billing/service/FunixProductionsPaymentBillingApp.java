package com.funixproductions.api.payment.billing.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.user",
        "com.funixproductions.api.google"
})
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsPaymentBillingApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsPaymentBillingApp.class, args);
    }
}
