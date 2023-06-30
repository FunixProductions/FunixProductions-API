package com.funixproductions.api.twitch.reference.service.clients.chat;

import com.funixproductions.api.twitch.auth.client.configurations.TwitchApiRequestInterceptor;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChatAnnouncement;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "TwitchReferenceChatClient",
        url = "https://api.twitch.tv",
        configuration = TwitchApiRequestInterceptor.class,
        path = "helix/chat"
)
public interface TwitchReferenceChatClient {

    /**
     * Gets the list of users that are connected to the broadcaster’s chat session.
     * NOTE: There is a delay between when users join and leave a chat and when the list is updated accordingly.
     * To determine whether a user is a moderator or VIP, use the Get Moderators and Get VIPs endpoints.
     * You can check the roles of up to 100 users.
     * Requires a user access token that includes the moderator:read:chatters scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose list of chatters you want to get.
     * @param moderatorId The ID of the broadcaster or one of the broadcaster’s moderators. This ID must match the user ID in the user access token.
     * @param maxChattersReturned The maximum number of items to return per page in the response.
     *                            The minimum page size is 1 item per page and the maximum is 1,000. The default is 100.
     * @param paginationCursor The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return Chatters list
     */
    @GetMapping("chatters")
    TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                       @RequestParam(name = "broadcaster_id") String broadcasterId,
                                                                       @RequestParam(name = "moderator_id") String moderatorId,
                                                                       @RequestParam(name = "first", required = false, defaultValue = "100") Integer maxChattersReturned,
                                                                       @RequestParam(name = "after", required = false, defaultValue = "") String paginationCursor);

    /**
     * Sends an announcement to the broadcaster’s chat room.
     * Requires a user access token that includes the moderator:manage:announcements scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster that owns the chat room to send the announcement to.
     * @param moderatorId The ID of a user who has permission to moderate the broadcaster’s chat room,
     *                    or the broadcaster’s ID if they’re sending the announcement.
     *                    This ID must match the user ID in the user access token.
     */
    @PostMapping("announcements")
    void sendChatAnnouncement(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                              @RequestParam(name = "broadcaster_id") String broadcasterId,
                              @RequestParam(name = "moderator_id") String moderatorId,
                              @RequestBody TwitchChatAnnouncement announcement);

}
