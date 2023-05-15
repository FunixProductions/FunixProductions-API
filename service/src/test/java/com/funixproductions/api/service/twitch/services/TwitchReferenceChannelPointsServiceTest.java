package com.funixproductions.api.service.twitch.services;

import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.service.twitch.reference.clients.channel.TwitchReferenceChannelPointsClient;
import com.funixproductions.api.service.twitch.reference.services.channel.TwitchReferenceChannelPointsService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TwitchReferenceChannelPointsServiceTest {

    @MockBean
    private TwitchReferenceChannelPointsClient client;

    @Autowired
    private TwitchReferenceChannelPointsService service;

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
