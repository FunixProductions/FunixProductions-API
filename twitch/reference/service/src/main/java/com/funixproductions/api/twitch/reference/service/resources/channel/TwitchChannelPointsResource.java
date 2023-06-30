package com.funixproductions.api.twitch.reference.service.resources.channel;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.channel.TwitchChannelPointsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import com.funixproductions.api.twitch.reference.service.resources.TwitchReferenceResource;
import com.funixproductions.api.twitch.reference.service.services.channel.TwitchReferenceChannelPointsService;
import com.funixproductions.api.user.client.security.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/channel/custom_rewards")
public class TwitchChannelPointsResource extends TwitchReferenceResource implements TwitchChannelPointsClient {

    private final TwitchReferenceChannelPointsService service;

    public TwitchChannelPointsResource(CurrentSession currentSession,
                                       TwitchInternalAuthClient tokenService,
                                       TwitchReferenceChannelPointsService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards() {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return service.getChannelRewards(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId()
        );
    }
}
