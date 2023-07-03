package com.funixproductions.api.twitch.eventsub.service.services;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.eventsub.client.dtos.TwitchEventSubListDTO;
import com.funixproductions.api.twitch.eventsub.service.entities.TwitchEventSubStreamer;
import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;
import com.funixproductions.api.twitch.eventsub.service.repositories.TwitchEventSubStreamerRepository;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchEventSubRegistrationServiceTest {

    @Autowired
    private TwitchEventSubRegistrationService service;

    @MockBean
    private TwitchUsersClient twitchReferenceUsersService;

    @MockBean
    private TwitchEventSubReferenceService twitchEventSubReferenceService;

    @MockBean
    private TwitchInternalAuthClient twitchServerTokenService;

    @MockBean
    private TwitchEventSubStreamerRepository repository;

    private final int eventCount = ChannelEventType.values().length;

    @BeforeEach
    void setupMocks() {
        setupTwitchEventSubReferenceServiceMocks();
        setupTwitchReferenceUsersServiceMocks();
        setupTwitchServerTokenServiceMocks();
    }

    @Test
    void updateSubscriptionAsyncMethodTest() {
        log.info("--- STARTED TEST updateSubscriptionAsyncMethodTest ---");
        service.updateSubscriptions(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        log.info("--- DONE TEST updateSubscriptionAsyncMethodTest ---");

        verify(twitchEventSubReferenceService, times(eventCount)).createSubscription(any());
    }

    @Test
    void removeStreamerSubscriptionsTest() {
        log.info("--- STARTED TEST removeStreamerSubscriptions ---");
        service.removeStreamerSubscriptions(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        log.info("--- DONE TEST removeStreamerSubscriptions ---");

        verify(twitchEventSubReferenceService, times(8)).deleteSubscription(any());
    }

    @Test
    void checkStreamerSubscriptionsTest() {
        final TwitchEventSubStreamer subStreamer = new TwitchEventSubStreamer();
        subStreamer.setStreamerId(UUID.randomUUID().toString());
        repository.save(subStreamer);

        service.checkStreamersSubscriptions();
    }

    @Test
    void createSubscriptionsFromResource() {
        final String streamerUsername = UUID.randomUUID().toString();

        log.info("--- STARTED TEST createSubscriptionsFromResource ---");
        service.createSubscription(streamerUsername);
        log.info("--- DONE TEST createSubscriptionsFromResource ---");

        try {
            service.createSubscription(streamerUsername);
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    void deleteSubscriptionsFromResource() {
        final String streamerUsername = UUID.randomUUID().toString();

        log.info("--- STARTED TEST createSubscriptionsFromResource ---");
        service.removeSubscription(streamerUsername);
        log.info("--- DONE TEST createSubscriptionsFromResource ---");
    }

    private void setupTwitchEventSubReferenceServiceMocks() {
        final Random random = new Random();
        final List<TwitchEventSubListDTO.TwitchEventSub> eventSubs = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            final TwitchEventSubListDTO.TwitchEventSub eventSub = new TwitchEventSubListDTO.TwitchEventSub();
            eventSub.setCondition(new TwitchEventSubListDTO.Condition());
            eventSub.setCost(random.nextInt());
            eventSub.setType(UUID.randomUUID().toString());
            eventSub.setStatus("active");
            eventSub.setId(UUID.randomUUID().toString());
            eventSub.setCreatedAt(new Date());
            eventSub.setVersion(UUID.randomUUID().toString());
            eventSubs.add(eventSub);
        }
        final TwitchEventSubListDTO.TwitchEventSub eventSub = new TwitchEventSubListDTO.TwitchEventSub();
        eventSub.setCondition(new TwitchEventSubListDTO.Condition());
        eventSub.setCost(random.nextInt());
        eventSub.setType(UUID.randomUUID().toString());
        eventSub.setStatus("not-active");
        eventSub.setId(UUID.randomUUID().toString());
        eventSub.setCreatedAt(new Date());
        eventSub.setVersion(UUID.randomUUID().toString());
        eventSubs.add(eventSub);

        final TwitchEventSubListDTO twitchEventSubListDTONoPage = new TwitchEventSubListDTO();
        twitchEventSubListDTONoPage.setPagination(null);
        twitchEventSubListDTONoPage.setTotal(4);
        twitchEventSubListDTONoPage.setTotalCost(10);
        twitchEventSubListDTONoPage.setMaxTotalCost(100);
        twitchEventSubListDTONoPage.setData(eventSubs);

        final TwitchEventSubListDTO twitchEventSubListDTOWithPage = new TwitchEventSubListDTO();
        twitchEventSubListDTOWithPage.setPagination(new TwitchEventSubListDTO.Pagination());
        twitchEventSubListDTOWithPage.getPagination().setCursor("data");
        twitchEventSubListDTOWithPage.setTotal(4);
        twitchEventSubListDTOWithPage.setTotalCost(10);
        twitchEventSubListDTOWithPage.setMaxTotalCost(100);
        twitchEventSubListDTOWithPage.setData(eventSubs);

        when(twitchEventSubReferenceService.getSubscriptions(any(), any(), anyString(), isNull())).thenReturn(twitchEventSubListDTOWithPage);
        when(twitchEventSubReferenceService.getSubscriptions(any(), any(), anyString(), anyString())).thenReturn(twitchEventSubListDTONoPage);
        doNothing().when(twitchEventSubReferenceService).createSubscription(any());
        doNothing().when(twitchEventSubReferenceService).deleteSubscription(any());
    }

    private void setupTwitchReferenceUsersServiceMocks() {
        final TwitchDataResponseDTO<TwitchUserDTO> userList = new TwitchDataResponseDTO<>();
        final TwitchUserDTO userDTO = new TwitchUserDTO();
        userDTO.setId(UUID.randomUUID().toString());
        userList.setData(List.of(userDTO));

        when(twitchReferenceUsersService.getUsersByName(anyList())).thenReturn(userList);
    }

    private void setupTwitchServerTokenServiceMocks() {
        when(twitchServerTokenService.fetchServerToken()).thenReturn(UUID.randomUUID().toString());
    }

}
