package com.funixproductions.api.twitch.reference.client.clients.channel;

import com.funixproductions.api.client.core.config.FeignConfig;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "TwitchChannelPointsClient",
        url = "${funixproductions.api.app-domain-url}",
        path = "twitch/channel/custom_rewards",
        configuration = FeignConfig.class
)
public interface TwitchChannelPointsClient {

    /**
     * Gets a list of custom rewards that the specified broadcaster created.
     * Requires a user access token that includes the channel:read:redemptions scope.
     * NOTE: A channel may offer a maximum of 50 rewards, which includes both enabled and disabled rewards.
     * @return reward information
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards();

}
