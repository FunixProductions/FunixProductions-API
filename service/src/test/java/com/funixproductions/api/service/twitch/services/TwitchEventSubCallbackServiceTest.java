package com.funixproductions.api.service.twitch.services;

import com.funixproductions.api.client.twitch.eventsub.dtos.events.channel.TwitchEventChannelFollowDTO;
import com.funixproductions.api.service.twitch.eventsub.services.TwitchEventSubCallbackService;
import com.funixproductions.api.service.twitch.eventsub.services.TwitchEventSubHmacService;
import com.funixproductions.api.service.twitch.eventsub.services.handler.TwitchEventSubHandlerService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchEventSubCallbackServiceTest {

    @InjectMocks
    private TwitchEventSubCallbackService service;

    @Mock
    private TwitchEventSubHmacService hmacService;

    @Mock
    private TwitchEventSubHandlerService handlerService;

    private final Gson gson = new Gson();

    @Test
    void testNewWebhookNotification() throws RuntimeException {
        doNothing().when(hmacService).validEventMessage(any(), any());
        doNothing().when(handlerService).receiveNewNotification(any(), any());

        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, "10");
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_NOTIFICATION);

        final TwitchEventChannelFollowDTO followDTO = new TwitchEventChannelFollowDTO();
        final TwitchBodyTestNotificationTest<TwitchEventChannelFollowDTO> test = new TwitchBodyTestNotificationTest<>(followDTO);
        assertDoesNotThrow(() -> {
            service.handleNewWebhook(httpServletRequest, gson.toJson(test).getBytes(StandardCharsets.UTF_8));
        });
        assertThrows(ApiBadRequestException.class, () ->
                service.handleNewWebhook(httpServletRequest, gson.toJson(test).getBytes(StandardCharsets.UTF_8))
        );

        service.cleanMessagesIds();
    }

    @Test
    void testNewWebhookVerification() throws ApiException {
        doNothing().when(hmacService).validEventMessage(any(), any());

        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, "11");
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_VERIFICATION);

        final TwitchBodyTestVerificationTest verification = new TwitchBodyTestVerificationTest();
        final String response = service.handleNewWebhook(httpServletRequest, gson.toJson(verification).getBytes(StandardCharsets.UTF_8));
        assertEquals(verification.challenge, response);
    }

    @Test
    void testNewWebhookRevocation() throws ApiException {
        doNothing().when(hmacService).validEventMessage(any(), any());

        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, "12");
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_REVOCATION);

        final String s = service.handleNewWebhook(httpServletRequest, "anything".getBytes(StandardCharsets.UTF_8));
        assertEquals("s", s);
    }

    @Test
    void missingMessageIdOrMessageType() {
        assertThrows(ApiBadRequestException.class, () -> {
            doNothing().when(hmacService).validEventMessage(any(), any());
            doNothing().when(handlerService).receiveNewNotification(any(), any());
            final MockHttpServletRequest request = new MockHttpServletRequest();

            service.handleNewWebhook(request, "".getBytes());

            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, UUID.randomUUID().toString());

            service.handleNewWebhook(request, "".getBytes());
        });
    }

    @Test
    void testMalformatedBodyNotification() {
        assertThrows(ApiBadRequestException.class, () -> {
            doNothing().when(hmacService).validEventMessage(any(), any());
            doNothing().when(handlerService).receiveNewNotification(any(), any());
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, UUID.randomUUID().toString());
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_NOTIFICATION);

            service.handleNewWebhook(request, "".getBytes());
        });
    }

    @Test
    void testMalformatedBodyVerification() {
        assertThrows(ApiBadRequestException.class, () -> {
            doNothing().when(hmacService).validEventMessage(any(), any());
            doNothing().when(handlerService).receiveNewNotification(any(), any());
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, UUID.randomUUID().toString());
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_VERIFICATION);

            service.handleNewWebhook(request, "".getBytes());
        });
    }

    @Getter
    private static class TwitchBodyTestVerificationTest {
        private final String challenge = UUID.randomUUID().toString();
    }

    @Getter
    @RequiredArgsConstructor
    private static class TwitchBodyTestNotificationTest<T> {

        private final SubscriptionTest subscription = new SubscriptionTest();

        private final T event;

        @Getter
        private static class SubscriptionTest {

            private final String id = UUID.randomUUID().toString();

            private final String type = UUID.randomUUID().toString();

        }

    }

}
