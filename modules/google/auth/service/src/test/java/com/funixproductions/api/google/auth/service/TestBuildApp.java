package com.funixproductions.api.google.auth.service;

import com.funixproductions.api.google.auth.service.config.GoogleAuthConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class TestBuildApp {

    @Autowired
    private GoogleAuthConfig googleAuthConfig;

    @Test
    void testLoadContext() {
        assertEquals("client-1", googleAuthConfig.getClientId()[0]);
        assertEquals("client-2", googleAuthConfig.getClientId()[1]);
        log.info("test");
    }

}
