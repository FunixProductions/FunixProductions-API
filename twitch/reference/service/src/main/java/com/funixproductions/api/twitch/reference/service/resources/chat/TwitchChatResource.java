package com.funixproductions.api.twitch.reference.service.resources.chat;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.chat.TwitchChatClient;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChatAnnouncement;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.twitch.reference.service.resources.TwitchReferenceResource;
import com.funixproductions.api.twitch.reference.service.services.chat.TwitchReferenceChatService;
import com.funixproductions.api.user.client.security.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/chat")
public class TwitchChatResource extends TwitchReferenceResource implements TwitchChatClient {

    private final TwitchReferenceChatService service;

    public TwitchChatResource(CurrentSession currentSession,
                              TwitchInternalAuthClient tokenService,
                              TwitchReferenceChatService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(Integer maxChattersReturned,
                                                                              String paginationCursor) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return service.getChannelChatters(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                tokenDTO.getTwitchUserId(),
                maxChattersReturned,
                paginationCursor
        );
    }

    @Override
    public void sendChatAnnouncement(TwitchChatAnnouncement announcement) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        service.sendChatAnnouncement(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                tokenDTO.getTwitchUserId(),
                announcement
        );
    }
}
