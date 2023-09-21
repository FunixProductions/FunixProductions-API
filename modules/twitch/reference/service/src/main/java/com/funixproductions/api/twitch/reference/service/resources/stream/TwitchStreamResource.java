package com.funixproductions.api.twitch.reference.service.resources.stream;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.api.twitch.reference.service.services.stream.TwitchReferenceStreamService;
import com.funixproductions.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/reference/streams")
@RequiredArgsConstructor
public class TwitchStreamResource implements TwitchStreamsClient {

    private final TwitchReferenceStreamService streamService;
    private final TwitchInternalAuthClient internalAuthClient;

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStreams(String streamerName) {
        try {
            return streamService.getStreams(
                    internalAuthClient.fetchServerToken(),
                    streamerName
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }
}
