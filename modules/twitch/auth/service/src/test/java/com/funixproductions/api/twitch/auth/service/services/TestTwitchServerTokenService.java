package com.funixproductions.api.twitch.auth.service.services;

import com.funixproductions.api.twitch.auth.service.clients.TwitchTokenAuthClient;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchTokenResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TestTwitchServerTokenService {

    @MockBean
    private TwitchTokenAuthClient twitchTokenAuthClient;

    @Autowired
    private TwitchServerTokenService twitchServerTokenService;

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
