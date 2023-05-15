package com.funixproductions.api.service.twitch.auth.services;

import com.funixproductions.api.service.twitch.auth.clients.TwitchTokenAuthClient;
import com.funixproductions.api.service.twitch.auth.dtos.TwitchTokenResponseDTO;
import com.funixproductions.api.service.twitch.configs.TwitchApiConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TestTwitchServerTokenService {

    @Mock
    private TwitchTokenAuthClient twitchTokenAuthClient;

    private TwitchServerTokenService twitchServerTokenService;

    @Autowired
    private TwitchApiConfig twitchApiConfig;

    @BeforeEach
    void init() {
        twitchServerTokenService = new TwitchServerTokenService(twitchApiConfig, twitchTokenAuthClient);
    }

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
    }
}
