package com.funixproductions.api.twitch.reference.service.resources.channel;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.channel.TwitchChannelClient;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.TwitchChannelDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelUserDTO;
import com.funixproductions.api.twitch.reference.service.services.channel.TwitchReferenceChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch/reference/channel")
@RequiredArgsConstructor
public class TwitchChannelResource implements TwitchChannelClient {

    private final TwitchInternalAuthClient internalAuthClient;
    private final TwitchReferenceChannelService service;

    @Override
    public TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(List<String> broadcasterId) {
        return service.getChannelInformation(
                internalAuthClient.fetchServerToken(),
                broadcasterId
        );
    }

    @Override
    public void updateChannelInformation(TwitchChannelUpdateDTO channelUpdateDTO, String userId) {
        final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId);

        service.updateChannelInformation(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                channelUpdateDTO
        );
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(String maximumReturned, String after, List<String> userIds, String userId) {
        final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId);

        return service.getChannelVips(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                maximumReturned,
                after,
                userIds
        );
    }
}
