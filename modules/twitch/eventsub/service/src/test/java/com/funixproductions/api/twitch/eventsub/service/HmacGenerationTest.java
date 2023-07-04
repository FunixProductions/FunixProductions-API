package com.funixproductions.api.twitch.eventsub.service;

import com.funixproductions.api.twitch.eventsub.service.services.TwitchEventSubHmacService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class HmacGenerationTest {

    @Test
    public void testHmacGeneration() {
        final String hmacKey = TwitchEventSubHmacService.generateNewSecretForHmac();

        log.info("Hmac key: {}", hmacKey);
        assertFalse(Strings.isBlank(hmacKey));
    }

}
