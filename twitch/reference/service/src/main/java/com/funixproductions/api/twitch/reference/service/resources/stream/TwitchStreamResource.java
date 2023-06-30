package com.funixproductions.api.twitch.reference.service.resources.stream;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.api.twitch.reference.service.resources.TwitchReferenceResource;
import com.funixproductions.api.twitch.reference.service.services.stream.TwitchReferenceStreamService;
import com.funixproductions.api.user.client.security.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/streams")
public class TwitchStreamResource extends TwitchReferenceResource implements TwitchStreamsClient {

    private final TwitchReferenceStreamService streamService;

    public TwitchStreamResource(CurrentSession currentSession,
                                TwitchReferenceStreamService streamService,
                                TwitchInternalAuthClient clientTokenService) {
        super(clientTokenService, currentSession);
        this.streamService = streamService;
    }

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStreams(String streamerName) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return streamService.getStreams(
                tokenDTO.getAccessToken(),
                streamerName
        );
    }
}
