package com.funixproductions.api.twitch.reference.service.resources.channel;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.reference.clients.channel.TwitchChannelPointsClient;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.twitch.reference.resources.TwitchReferenceResource;
import com.funixproductions.api.service.twitch.reference.services.channel.TwitchReferenceChannelPointsService;
import com.funixproductions.api.service.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/channel/custom_rewards")
public class TwitchChannelPointsResource extends TwitchReferenceResource implements TwitchChannelPointsClient {

    private final TwitchReferenceChannelPointsService service;

    public TwitchChannelPointsResource(CurrentSession currentSession,
                                       TwitchClientTokenService tokenService,
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
