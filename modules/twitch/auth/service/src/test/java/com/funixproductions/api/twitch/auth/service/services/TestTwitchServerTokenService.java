package com.funixproductions.api.twitch.auth.service.services;

import com.funixproductions.api.twitch.auth.service.clients.TwitchTokenAuthClient;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchTokenResponseDTO;
import com.funixproductions.api.twitch.eventsub.client.clients.TwitchEventSubInternalClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TestTwitchServerTokenService {

    @MockitoBean
    private TwitchTokenAuthClient twitchTokenAuthClient;

    @Autowired
    private TwitchServerTokenService twitchServerTokenService;

    @MockitoBean
    private TwitchEventSubInternalClient twitchEventSubInternalClient;

    @Test
    void testFetchingToken() throws Exception {
        final TwitchTokenResponseDTO mockToken = new TwitchTokenResponseDTO();
        mockToken.setAccessToken("access");
        mockToken.setRefreshToken("refersh");
        mockToken.setTokenType("bearer");
        mockToken.setExpiresIn(3000);

        Mockito.when(twitchTokenAuthClient.getToken(Mockito.any())).thenReturn(mockToken);

        twitchServerTokenService.refreshToken();

        assertNotNull(twitchServerTokenService.getAccessToken());
        assertNotNull(twitchServerTokenService.getExpiresAt());

        Mockito.doNothing().when(twitchEventSubInternalClient).createSubscription(anyString());
    }
}
