package com.funixproductions.api.twitch.reference.service.services;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.service.clients.channel.TwitchReferenceChannelPointsClient;
import com.funixproductions.api.twitch.reference.service.services.channel.TwitchReferenceChannelPointsService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchReferenceChannelPointsServiceTest {

    @MockitoBean
    private TwitchReferenceChannelPointsClient client;

    @Autowired
    private TwitchReferenceChannelPointsService service;

    @MockitoBean
    private TwitchInternalAuthClient internalAuthClient;

    @BeforeEach
    void setupMocks() {
        final TwitchClientTokenDTO twitchClientTokenDTO = new TwitchClientTokenDTO();
        twitchClientTokenDTO.setAccessToken(UUID.randomUUID().toString());
        twitchClientTokenDTO.setTwitchUserId(UUID.randomUUID().toString());
        twitchClientTokenDTO.setTwitchUsername(UUID.randomUUID().toString());
        twitchClientTokenDTO.setExpirationDateToken(Date.from(Instant.now().plusSeconds(1000)));

        when(internalAuthClient.fetchServerToken()).thenReturn(UUID.randomUUID().toString());
        when(internalAuthClient.fetchToken(anyString())).thenReturn(twitchClientTokenDTO);
    }

    @Test
    void testFetchRewards() {
        when(client.getChannelRewards(anyString(), anyString())).thenReturn(new TwitchDataResponseDTO<>());
        assertNotNull(service.getChannelRewards("token", "10"));
    }

    @Test
    void testThrow400() {
        final FeignException exception = new FeignException.BadRequest("test error", mockRequest(), null, null);
        when(client.getChannelRewards(anyString(), anyString())).thenThrow(exception);

        assertThrows(ApiBadRequestException.class, () -> service.getChannelRewards("token", "10"));
    }

    @Test
    void testThrow401() {
        final FeignException exception = new FeignException.Unauthorized("test error", mockRequest(), null, null);
        when(client.getChannelRewards(anyString(), anyString())).thenThrow(exception);

        assertThrows(ApiBadRequestException.class, () -> service.getChannelRewards("token", "10"));
    }

    @Test
    void testThrow403() {
        final FeignException exception = new FeignException.Forbidden("test error", mockRequest(), null, null);
        when(client.getChannelRewards(anyString(), anyString())).thenThrow(exception);

        assertThrows(ApiBadRequestException.class, () -> service.getChannelRewards("token", "10"));
    }

    @Test
    void testThrow404() {
        final FeignException exception = new FeignException.NotFound("test error", mockRequest(), null, null);
        when(client.getChannelRewards(anyString(), anyString())).thenThrow(exception);

        assertThrows(ApiBadRequestException.class, () -> service.getChannelRewards("token", "10"));
    }

    @Test
    void testThrow429() {
        final FeignException exception = new FeignException.TooManyRequests("test error", mockRequest(), null, null);
        when(client.getChannelRewards(anyString(), anyString())).thenThrow(exception);

        assertThrows(ApiBadRequestException.class, () -> service.getChannelRewards("token", "10"));
    }

    private Request mockRequest() {
        return Request.create(Request.HttpMethod.GET, "mockUrl", new HashMap<>(), null, new RequestTemplate());
    }

}
