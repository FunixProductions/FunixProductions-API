package com.funixproductions.api.twitch.reference.service.clients.channel;

import com.funixproductions.api.twitch.auth.client.configurations.TwitchApiRequestInterceptor;
import com.funixproductions.api.twitch.reference.client.dtos.requests.TwitchChannelUpdateDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.TwitchChannelDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelUserDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <a href="https://dev.twitch.tv/docs/api/reference#get-channel-information">Doc twitch</a>
 */
@FeignClient(
        name = "TwitchReferenceChannelClient",
        url = "https://api.twitch.tv",
        configuration = TwitchApiRequestInterceptor.class,
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

    /**
     * <p>Gets a list of users that follow the specified broadcaster. You can also use this endpoint to see whether a specific user follows the broadcaster.</p>
     * <p>This endpoint will return specific follower information only if both of the above are true. If a scope is not provided or the user isn’t the broadcaster or a moderator for the specified channel, only the total follower count will be included in the response.</p>
     * <p>Requires a user access token.</p>
     * @param twitchAccessToken Requires a user access token that includes the moderator:read:followers scope.
     * @param streamerId The broadcaster’s ID. Returns the list of users that follow this broadcaster.
     * @param maximumReturned The maximum number of items to return per page in the response. The minimum page size is 1 item per page and the maximum is 100. The default is 20.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value. Read more.
     * @param userId A user’s ID. Use this parameter to see whether the user follows this broadcaster. If specified, the response contains this user if they follow the broadcaster. If not specified, the response contains all users that follow the broadcaster.
     * @return The list of users that follow the specified broadcaster. The list is in descending order by followed_at (with the most recent follower first). The list is empty if nobody follows the broadcaster, the specified user_id isn’t in the follower list, the user access token is missing the moderator:read:followers scope, or the user isn’t the broadcaster or moderator for the channel.
     */
    @GetMapping("followers")
    TwitchDataResponseDTO<TwitchFollowDTO> getChannelFollowers(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                               @RequestParam(name = "broadcaster_id") String streamerId,
                                                               @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                               @RequestParam(name = "after", required = false, defaultValue = "20") String after,
                                                               @RequestParam(name = "user_id", required = false) String userId);

}
