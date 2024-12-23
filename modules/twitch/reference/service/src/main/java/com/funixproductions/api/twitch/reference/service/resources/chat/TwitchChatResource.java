package com.funixproductions.api.twitch.reference.service.resources.chat;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.reference.client.clients.chat.TwitchChatClient;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChatAnnouncement;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.twitch.reference.service.services.chat.TwitchReferenceChatService;
import com.funixproductions.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/reference/chat")
@RequiredArgsConstructor
public class TwitchChatResource implements TwitchChatClient {

    private final TwitchReferenceChatService service;
    private final TwitchInternalAuthClient internalAuthClient;

    @Override
    public TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(Integer maxChattersReturned,
                                                                              String paginationCursor,
                                                                              String userId) {
        try {
            final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId, TwitchClientTokenType.STREAMER.name());

            return service.getChannelChatters(
                    tokenDTO.getAccessToken(),
                    tokenDTO.getTwitchUserId(),
                    tokenDTO.getTwitchUserId(),
                    maxChattersReturned,
                    paginationCursor
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }

    @Override
    public void sendChatAnnouncement(TwitchChatAnnouncement announcement, String userId) {
        try {
            final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId, TwitchClientTokenType.FUNIXGAMING.name());

            service.sendChatAnnouncement(
                    tokenDTO.getAccessToken(),
                    tokenDTO.getTwitchUserId(),
                    tokenDTO.getTwitchUserId(),
                    announcement
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }
}
