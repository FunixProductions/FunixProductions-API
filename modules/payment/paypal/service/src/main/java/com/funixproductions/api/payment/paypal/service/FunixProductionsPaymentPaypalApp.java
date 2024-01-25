package com.funixproductions.api.payment.paypal.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.payment.paypal.service",
        "com.funixproductions.api.payment.billing.client"
})
@SpringBootApplication(scanBasePackages = "com.funixproductions")
public class FunixProductionsPaymentPaypalApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsPaymentPaypalApp.class, args);
    }
}
