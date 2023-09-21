package com.funixproductions.api.twitch.reference.service.services.video;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.video.TwitchChannelClipCreationDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.video.TwitchChannelClipDTO;
import com.funixproductions.api.twitch.reference.client.services.TwitchReferenceService;
import com.funixproductions.api.twitch.reference.service.clients.video.TwitchReferenceClipsClient;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.google.common.base.Strings;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TwitchReferenceClipsService extends TwitchReferenceService implements TwitchReferenceClipsClient {

    private final TwitchReferenceClipsClient client;

    @Override
    public TwitchChannelClipCreationDTO createClip(@NonNull final String twitchAccessToken,
                                                   @NonNull final String broadcasterId) {
        try {
            return client.createClip(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelClipDTO> getStreamerClips(@NonNull final String twitchAccessToken,
                                                                        @NonNull final String broadcasterId,
                                                                        @Nullable final Date startedAt,
                                                                        @Nullable final Date endedAt,
                                                                        @Nullable final Integer amountReturned,
                                                                        @Nullable final String before,
                                                                        @Nullable final String after) {
        try {
            return client.getStreamerClips(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId,
                    startedAt,
                    endedAt,
                    amountReturned == null || amountReturned < 0 ? null : amountReturned,
                    Strings.isNullOrEmpty(before) ? null : before,
                    Strings.isNullOrEmpty(after) ? null : after
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelClipDTO> getClipsById(@NonNull final String twitchAccessToken,
                                                                    @NonNull final Date startedAt,
                                                                    @NonNull final Date endedAt,
                                                                    @Nullable final Integer amountReturned,
                                                                    @Nullable final String before,
                                                                    @Nullable final String after,
                                                                    @NonNull final List<String> id) {
        if (id.isEmpty()) {
            throw new ApiBadRequestException("La liste d'id de clips est vide.");
        }

        try {
            return client.getClipsById(
                    super.addBearerPrefix(twitchAccessToken),
                    startedAt,
                    endedAt,
                    amountReturned == null || amountReturned < 0 ? null : amountReturned,
                    Strings.isNullOrEmpty(before) ? null : before,
                    Strings.isNullOrEmpty(after) ? null : after,
                    id
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
