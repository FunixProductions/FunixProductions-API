package com.funixproductions.api.service.twitch.services;

import com.funixproductions.api.service.twitch.eventsub.services.TwitchEventSubHmacService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureMockMvc
class TwitchEventSubHmacServiceTest {

    @Autowired
    private TwitchEventSubHmacService hmacService;

    @Test
    @Order(1)
    void testValidTwitchCall() throws Exception {
        final File file = new File(TwitchEventSubHmacService.FILE_SECRET_NAME);
        file.delete();

        final String date = Instant.now().toString();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_ID, "10");
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_TIMESTAMP, date);
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_SIGNATURE, "sha256=" + encode("10" + date + "body"));

        assertDoesNotThrow(() ->
                hmacService.validEventMessage(request, "body".getBytes(StandardCharsets.UTF_8))
        );
    }

    @Test
    @Order(2)
    void testValidTwitchCallWithFile() throws Exception {
        final String date = Instant.now().toString();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_ID, "10");
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_TIMESTAMP, date);
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_SIGNATURE, "sha256=" + encode("10" + date + "body"));

        assertDoesNotThrow(() ->
                hmacService.validEventMessage(request, "body".getBytes(StandardCharsets.UTF_8))
        );
    }

    private String encode(final String data) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(hmacService.getKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);

        return bytesToHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
