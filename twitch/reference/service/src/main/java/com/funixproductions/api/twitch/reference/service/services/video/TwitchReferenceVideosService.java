package com.funixproductions.api.twitch.reference.service.services.video;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.video.TwitchChannelVideoDTO;
import com.funixproductions.api.twitch.reference.service.clients.video.TwitchReferenceVideosClient;
import com.funixproductions.api.twitch.reference.service.services.TwitchReferenceService;
import com.google.common.base.Strings;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceVideosService extends TwitchReferenceService implements TwitchReferenceVideosClient {

    private final TwitchReferenceVideosClient client;

    public TwitchDataResponseDTO<TwitchChannelVideoDTO> getStreamerVods(@NonNull final String twitchAccessToken,
                                                                        @NonNull final String streamerId,
                                                                        @Nullable final Integer amountReturned,
                                                                        @Nullable final String before,
                                                                        @Nullable final String after) {
        try {
            return client.getStreamerVods(
                    super.addBearerPrefix(twitchAccessToken),
                    streamerId,
                    amountReturned == null || amountReturned < 0 ? null : amountReturned,
                    Strings.isNullOrEmpty(before) ? null : before,
                    Strings.isNullOrEmpty(after) ? null : after
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
