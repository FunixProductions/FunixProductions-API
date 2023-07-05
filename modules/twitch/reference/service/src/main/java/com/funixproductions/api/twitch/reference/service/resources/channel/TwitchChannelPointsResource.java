package com.funixproductions.api.twitch.reference.service.resources.channel;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.channel.TwitchChannelPointsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import com.funixproductions.api.twitch.reference.service.services.channel.TwitchReferenceChannelPointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/reference/channel/custom_rewards")
@RequiredArgsConstructor
public class TwitchChannelPointsResource implements TwitchChannelPointsClient {

    private final TwitchReferenceChannelPointsService service;
    private final TwitchInternalAuthClient internalAuthClient;

    @Override
    public TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards(String userId) {
        final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId);

        return service.getChannelRewards(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId()
        );
    }
}
