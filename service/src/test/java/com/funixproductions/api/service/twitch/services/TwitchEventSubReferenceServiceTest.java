package com.funixproductions.api.service.twitch.services;

import com.funixproductions.api.client.twitch.eventsub.dtos.TwitchEventSubListDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchServerTokenService;
import com.funixproductions.api.service.twitch.configs.TwitchApiConfig;
import com.funixproductions.api.service.twitch.eventsub.clients.TwitchEventSubReferenceClient;
import com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel.ChannelFollowSubscription;
import com.funixproductions.api.service.twitch.eventsub.services.TwitchEventSubHmacService;
import com.funixproductions.api.service.twitch.eventsub.services.TwitchEventSubReferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchEventSubReferenceServiceTest {

    @InjectMocks
    private TwitchEventSubReferenceService service;

    @Mock
    private TwitchEventSubHmacService hmacService;

    @Mock
    private TwitchServerTokenService tokenService;

    @Mock
    private TwitchEventSubReferenceClient referenceClient;

    @Mock
    private TwitchApiConfig twitchApiConfig;

    @BeforeEach
    void setupMocks() {
        when(hmacService.getKey()).thenReturn("key");
        when(tokenService.getAccessToken()).thenReturn("access");
        when(referenceClient.getSubscriptions(any(), any(), any(), any(), any())).thenReturn(new TwitchEventSubListDTO());
        doNothing().when(referenceClient).createSubscription(any(), any(), any());
        doNothing().when(referenceClient).deleteSubscription(any(), any());
        when(twitchApiConfig.getAppEventSubCallback()).thenReturn("domain");
    }

    @Test
    void testGetSubscriptions() {
        try {
            service.getSubscriptions("", "", "", "");
        } catch (Exception e) {
            fail("No throws accepted here.", e);
        }
    }

    @Test
    void testCreateSubscriptions() {
        try {
            service.createSubscription(new ChannelFollowSubscription("data"));
        } catch (Exception e) {
            fail("No throws accepted here.", e);
        }
    }

    @Test
    void testDeleteSubscription() {
        try {
            service.deleteSubscription("data");
        } catch (Exception e) {
            fail("No throws accepted here.", e);
        }
    }

}
