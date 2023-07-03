package com.funixproductions.api.twitch.eventsub.service.services;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.eventsub.client.dtos.TwitchEventSubListDTO;
import com.funixproductions.api.twitch.eventsub.service.clients.TwitchEventSubReferenceClient;
import com.funixproductions.api.twitch.eventsub.service.requests.channel.ChannelFollowSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchEventSubReferenceServiceTest {

    @Autowired
    private TwitchEventSubReferenceService service;

    @MockBean
    private TwitchEventSubHmacService hmacService;

    @MockBean
    private TwitchInternalAuthClient tokenService;

    @MockBean
    private TwitchEventSubReferenceClient referenceClient;

    @BeforeEach
    void setupMocks() {
        when(hmacService.getKey()).thenReturn("key");
        when(tokenService.fetchServerToken()).thenReturn("token");
        when(tokenService.fetchToken(anyString())).thenReturn(new TwitchClientTokenDTO());
        when(referenceClient.getSubscriptions(any(), any(), any(), any(), any())).thenReturn(new TwitchEventSubListDTO());
        doNothing().when(referenceClient).createSubscription(any(), any(), any());
        doNothing().when(referenceClient).deleteSubscription(any(), any());
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
