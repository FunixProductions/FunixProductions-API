package com.funixproductions.api.service.twitch.reference.services.chat;

import com.funixproductions.api.client.twitch.reference.dtos.requests.TwitchChatAnnouncement;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceService;
import com.funixproductions.api.service.twitch.reference.clients.chat.TwitchReferenceChatClient;
import com.google.common.base.Strings;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceChatService extends TwitchReferenceService implements TwitchReferenceChatClient {

    private final TwitchReferenceChatClient client;

    @Override
    public TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(@NonNull final String twitchAccessToken,
                                                                              @NonNull final String broadcasterId,
                                                                              @NonNull final String moderatorId,
                                                                              @Nullable final Integer maxChattersReturned,
                                                                              @Nullable final String paginationCursor) {
        try {
            return client.getChannelChatters(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId,
                    moderatorId,
                    maxChattersReturned == null || maxChattersReturned < 0 ? null : maxChattersReturned,
                    Strings.isNullOrEmpty(paginationCursor) ? null : paginationCursor
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public void sendChatAnnouncement(@NonNull final String twitchAccessToken,
                                     @NonNull final String broadcasterId,
                                     @NonNull final String moderatorId,
                                     @NonNull final TwitchChatAnnouncement announcement) {
        try {
            client.sendChatAnnouncement(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId,
                    moderatorId,
                    announcement
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
