package com.funixproductions.api.twitch.reference.service.services;

import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.service.clients.channel.TwitchReferenceChannelClient;
import com.funixproductions.api.twitch.reference.service.services.channel.TwitchReferenceChannelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TwitchReferenceChannelServiceTest {

    @MockBean
    private TwitchReferenceChannelClient client;

    @Autowired
    private TwitchReferenceChannelService service;

    @Test
    void testGetChannelInfo() {
        when(client.getChannelInformation(anyString(), anyList())).thenReturn(new TwitchDataResponseDTO<>());
        assertNotNull(service.getChannelInformation("token", new ArrayList<>()));
    }

    @Test
    void testGetChannelVips() {
        when(client.getChannelVips(
                anyString(),
                anyString(),
                any(),
                any(),
                any()
        )).thenReturn(new TwitchDataResponseDTO<>());
        assertNotNull(service.getChannelVips("token", "id", null, null, null));
    }

    @Test
    void updateChannelTest() {
        doNothing().when(client).updateChannelInformation(anyString(), anyString(), any(TwitchChannelUpdateDTO.class));
        assertDoesNotThrow(() -> service.updateChannelInformation("token", "id", new TwitchChannelUpdateDTO()));
    }

}
