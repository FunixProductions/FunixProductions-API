package com.funixproductions.api.service.twitch.reference.resources.channel;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.reference.clients.channel.TwitchChannelClient;
import com.funixproductions.api.client.twitch.reference.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.TwitchChannelDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat.TwitchChannelUserDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.twitch.reference.resources.TwitchReferenceResource;
import com.funixproductions.api.service.twitch.reference.services.channel.TwitchReferenceChannelService;
import com.funixproductions.api.service.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch/channel")
public class TwitchChannelResource extends TwitchReferenceResource implements TwitchChannelClient {

    private final TwitchReferenceChannelService service;

    public TwitchChannelResource(CurrentSession currentSession,
                                 TwitchClientTokenService tokenService,
                                 TwitchReferenceChannelService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(List<String> broadcasterId) {
        return service.getChannelInformation(
                super.getTwitchAuthByUserConnected().getAccessToken(),
                broadcasterId
        );
    }

    @Override
    public void updateChannelInformation(TwitchChannelUpdateDTO channelUpdateDTO) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        service.updateChannelInformation(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                channelUpdateDTO
        );
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(String maximumReturned, String after, List<String> userIds) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return service.getChannelVips(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                maximumReturned,
                after,
                userIds
        );
    }
}