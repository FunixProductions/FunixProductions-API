package com.funixproductions.api.twitch.reference.service.resources.chat;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.reference.clients.chat.TwitchChatClient;
import com.funixproductions.api.client.twitch.reference.dtos.requests.TwitchChatAnnouncement;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.twitch.reference.resources.TwitchReferenceResource;
import com.funixproductions.api.service.twitch.reference.services.chat.TwitchReferenceChatService;
import com.funixproductions.api.service.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/chat")
public class TwitchChatResource extends TwitchReferenceResource implements TwitchChatClient {

    private final TwitchReferenceChatService service;

    public TwitchChatResource(CurrentSession currentSession,
                              TwitchClientTokenService tokenService,
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
