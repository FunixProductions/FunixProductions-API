package com.funixproductions.api.twitch.reference.client.clients.chat;

import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChatAnnouncement;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchChatClient",
        url = "${funixproductions.api.twitch.reference.app-domain-url}",
        path = "/kubeinternal/twitch/chat"
)
public interface TwitchChatClient {

    /**
     * Gets the list of users that are connected to the broadcaster’s chat session.
     * NOTE: There is a delay between when users join and leave a chat and when the list is updated accordingly.
     * To determine whether a user is a moderator or VIP, use the Get Moderators and Get VIPs endpoints.
     * You can check the roles of up to 100 users.
     * Requires a user access token that includes the moderator:read:chatters scope.
     * @param maxChattersReturned The maximum number of items to return per page in the response.
     *                            The minimum page size is 1 item per page and the maximum is 1,000. The default is 100.
     * @param paginationCursor The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return Chatters list
     */
    @GetMapping("chatters")
    TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(@RequestParam(name = "first", required = false, defaultValue = "100") Integer maxChattersReturned,
                                                                       @RequestParam(name = "after", required = false) String paginationCursor);

    /**
     * Sends an announcement to the broadcaster’s chat room.
     * Requires a user access token that includes the moderator:manage:announcements scope.
     */
    @PostMapping("announcements")
    void sendChatAnnouncement(@RequestBody TwitchChatAnnouncement announcement);

}
