package com.funixproductions.api.twitch.reference.service.resources.channel;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.reference.client.clients.channel.TwitchChannelClient;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.TwitchChannelDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelUserDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.service.services.channel.TwitchReferenceChannelService;
import com.funixproductions.core.exceptions.ApiException;
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
        try {
            return service.getChannelInformation(
                    internalAuthClient.fetchServerToken(),
                    broadcasterId
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }

    @Override
    public void updateChannelInformation(TwitchChannelUpdateDTO channelUpdateDTO, String userId) {
        try {
            final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId, TwitchClientTokenType.FUNIXGAMING.name());

            service.updateChannelInformation(
                    tokenDTO.getAccessToken(),
                    tokenDTO.getTwitchUserId(),
                    channelUpdateDTO
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(String maximumReturned, String after, List<String> userIds, String userId) {
        try {
            final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userId, TwitchClientTokenType.FUNIXGAMING.name());

            return service.getChannelVips(
                    tokenDTO.getAccessToken(),
                    tokenDTO.getTwitchUserId(),
                    maximumReturned,
                    after,
                    userIds
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> getChannelFollowers(String maximumReturned, String after, String userTwitchId, String userAppId) {
        try {
            final TwitchClientTokenDTO tokenDTO = this.internalAuthClient.fetchToken(userAppId, TwitchClientTokenType.STREAMER.name());

            return service.getChannelFollowers(
                    tokenDTO.getAccessToken(),
                    tokenDTO.getTwitchUserId(),
                    maximumReturned,
                    after,
                    userTwitchId
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
    }
}
