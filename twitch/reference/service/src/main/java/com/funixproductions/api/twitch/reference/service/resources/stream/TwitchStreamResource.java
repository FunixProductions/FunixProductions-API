package com.funixproductions.api.twitch.reference.service.resources.stream;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.reference.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.twitch.auth.services.TwitchServerTokenService;
import com.funixproductions.api.service.twitch.configs.TwitchApiConfig;
import com.funixproductions.api.service.twitch.reference.resources.TwitchReferenceResource;
import com.funixproductions.api.service.twitch.reference.services.stream.TwitchReferenceStreamService;
import com.funixproductions.api.service.user.services.CurrentSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/twitch/streams")
public class TwitchStreamResource extends TwitchReferenceResource implements TwitchStreamsClient {

    private final TwitchReferenceStreamService streamService;
    private final TwitchServerTokenService serverTokenService;
    private final TwitchApiConfig twitchApiConfig;

    private final TwitchDataResponseDTO<TwitchStreamDTO> funixStreamCache = new TwitchDataResponseDTO<>();

    public TwitchStreamResource(CurrentSession currentSession,
                                TwitchClientTokenService clientTokenService,
                                TwitchReferenceStreamService streamService,
                                TwitchServerTokenService serverTokenService,
                                TwitchApiConfig twitchApiConfig) {
        super(clientTokenService, currentSession);
        this.streamService = streamService;
        this.serverTokenService = serverTokenService;
        this.twitchApiConfig = twitchApiConfig;

        fetchFunixStreamData();
    }

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStreams(String streamerName) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return streamService.getStreams(
                tokenDTO.getAccessToken(),
                streamerName
        );
    }

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getFunixStream() {
        return funixStreamCache;
    }

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void fetchFunixStreamData() {
        final String accessToken = serverTokenService.getAccessToken();
        if (accessToken == null) {
            return;
        }

        final TwitchDataResponseDTO<TwitchStreamDTO> funixStreamStatus = streamService.getStreams(
                accessToken,
                twitchApiConfig.getStreamerUsername()
        );

        this.funixStreamCache.setData(funixStreamStatus.getData());
        this.funixStreamCache.setPagination(funixStreamStatus.getPagination());
        this.funixStreamCache.setTotal(funixStreamStatus.getTotal());
    }
}
