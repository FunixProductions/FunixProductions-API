package com.funixproductions.api.service.twitch.reference.services.stream;

import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceService;
import com.funixproductions.api.service.twitch.reference.clients.stream.TwitchReferenceStreamsClient;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceStreamService extends TwitchReferenceService implements TwitchReferenceStreamsClient {

    private final TwitchReferenceStreamsClient client;

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStreams(@NonNull final String twitchAccessToken,
                                                             @NonNull final String streamerName) {
        try {
            return client.getStreams(
                    super.addBearerPrefix(twitchAccessToken),
                    streamerName
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
