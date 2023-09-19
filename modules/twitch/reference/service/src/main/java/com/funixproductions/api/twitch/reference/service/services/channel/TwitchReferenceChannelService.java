package com.funixproductions.api.twitch.reference.service.services.channel;

import com.funixproductions.api.twitch.auth.client.services.TwitchReferenceService;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.TwitchChannelDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelUserDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.service.clients.channel.TwitchReferenceChannelClient;
import com.google.common.base.Strings;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwitchReferenceChannelService extends TwitchReferenceService implements TwitchReferenceChannelClient {

    private final TwitchReferenceChannelClient client;

    @Override
    public TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(@NonNull final String twitchAccessToken,
                                                                         @NonNull final List<String> broadcasterId) {
        try {
            return client.getChannelInformation(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(@NonNull final String twitchAccessToken,
                                                                      @NonNull final String streamerId,
                                                                      @Nullable final String maximumReturned,
                                                                      @Nullable final String after,
                                                                      @Nullable final List<String> userIds) {
        try {
            return client.getChannelVips(
                    super.addBearerPrefix(twitchAccessToken),
                    streamerId,
                    Strings.isNullOrEmpty(maximumReturned) ? "20" : maximumReturned,
                    Strings.isNullOrEmpty(after) ? null : after,
                    userIds
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> getChannelFollowers(String twitchAccessToken,
                                                                      String streamerId,
                                                                      String maximumReturned,
                                                                      String after,
                                                                      String userId) {
        try {
            return client.getChannelFollowers(
                    super.addBearerPrefix(twitchAccessToken),
                    streamerId,
                    Strings.isNullOrEmpty(maximumReturned) ? "20" : maximumReturned,
                    Strings.isNullOrEmpty(after) ? null : after,
                    userId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public void updateChannelInformation(@NonNull final String twitchAccessToken,
                                         @NonNull final String broadcasterId,
                                         @NonNull final TwitchChannelUpdateDTO channelUpdateDTO) {
        try {
            client.updateChannelInformation(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId,
                    channelUpdateDTO
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
