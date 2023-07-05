package com.funixproductions.api.twitch.reference.service.services.channel;

import com.funixproductions.api.twitch.auth.client.services.TwitchReferenceService;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import com.funixproductions.api.twitch.reference.service.clients.channel.TwitchReferenceChannelPointsClient;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceChannelPointsService extends TwitchReferenceService implements TwitchReferenceChannelPointsClient {

    private final TwitchReferenceChannelPointsClient client;

    @Override
    public TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards(@NonNull final String twitchAccessToken,
                                                                           @NonNull final String broadcasterId) {
        try {
            return client.getChannelRewards(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
