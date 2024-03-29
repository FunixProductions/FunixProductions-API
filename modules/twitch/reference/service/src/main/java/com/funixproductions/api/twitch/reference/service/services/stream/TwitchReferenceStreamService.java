package com.funixproductions.api.twitch.reference.service.services.stream;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.api.twitch.reference.client.services.TwitchReferenceService;
import com.funixproductions.api.twitch.reference.service.clients.stream.TwitchReferenceStreamsClient;
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
