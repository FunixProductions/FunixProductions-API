package com.funixproductions.api.service.twitch.reference.clients.channel;

import com.funixproductions.api.client.twitch.reference.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.TwitchChannelDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat.TwitchChannelUserDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <a href="https://dev.twitch.tv/docs/api/reference#get-channel-information">Doc twitch</a>
 */
@FeignClient(
        name = "TwitchReferenceChannelClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/channels"
)
public interface TwitchReferenceChannelClient {

    /**
     * <p>Gets information about one or more channels.</p>
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose channel you want to get. Can be multiple
     * @return Channel information
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                  @RequestParam(name = "broadcaster_id") List<String> broadcasterId);

    /**
     * Updates a channel’s properties.
     * Requires a user access token that includes the channel:manage:broadcast scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param channelUpdateDTO update infos
     */
    @PatchMapping
    void updateChannelInformation(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                  @RequestParam(name = "broadcaster_id") String broadcasterId,
                                  @RequestBody TwitchChannelUpdateDTO channelUpdateDTO);

    /**
     * Gets a list of the broadcaster’s VIPs.
     * Requires a user access token that includes the channel:read:vips scope.
     * If your app also adds and removes VIP status, you can use the channel:manage:vips scope instead.
     * @param twitchAccessToken Bearer {accessToken}
     * @param streamerId The ID of the broadcaster whose list of moderators you want to get. This ID must match the user ID in the access token.
     * @param maximumReturned The maximum number of items to return per page in the response.
     *                        The minimum page size is 1 item per page and the maximum is 100 items per page. The default is 20.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @param userIds Filters the list for specific VIPs. To specify more than one user, include the user_id parameter for each user to get.
     *                For example, user_id=1234 user_id=5678. The maximum number of IDs that you may specify is 100.
     *                Ignores the ID of those users in the list that aren’t VIPs.
     * @return vip list
     */
    @GetMapping("vips")
    TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                               @RequestParam(name = "broadcaster_id") String streamerId,
                                                               @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                               @RequestParam(name = "after", required = false, defaultValue = "20") String after,
                                                               @RequestParam(name = "user_id", required = false, defaultValue = "") List<String> userIds);

}
