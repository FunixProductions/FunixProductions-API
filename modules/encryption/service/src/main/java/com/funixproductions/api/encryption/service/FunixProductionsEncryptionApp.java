package com.funixproductions.api.encryption.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.funixproductions.core",
        "com.funixproductions.api.encryption.service"
})
public class FunixProductionsEncryptionApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixProductionsEncryptionApp.class, args);
    }
}
