package com.funixproductions.api.twitch.reference.service.clients.users;

import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.user.TwitchUserDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "TwitchReferenceUsersClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/users"
)
public interface TwitchReferenceUsersClient {

    /**
     * Gets information about one or more users.
     * To include the user’s verified email address in the response, you must use a user access token that includes the user:read:email scope.
     * @param twitchAccessToken Requires an app access token or user access token.
     * @param name name list to get
     * @return users
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                        @RequestParam(name = "login") List<String> name);

    /**
     * Gets information about one or more users.
     * To include the user’s verified email address in the response, you must use a user access token that includes the user:read:email scope.
     * @param twitchAccessToken Requires an app access token or user access token.
     * @param id id list to get
     * @return users
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                      @RequestParam(name = "id") List<String> id);

    /**
     * Fetch the follow list of a user
     * @param twitchAccessToken Requires an app access token or user access token: Bearer token
     * @param userId user id to fetch
     * @param maximumReturned The maximum number of items to return per page
     * @param cursorAfter The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return follow list
     */
    @GetMapping("follows")
    TwitchDataResponseDTO<TwitchFollowDTO> getUserFollowingList(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                @RequestParam(name = "from_id", required = false, defaultValue = "") String userId,
                                                                @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                                @RequestParam(name = "after", required = false, defaultValue = "") String cursorAfter);

    /**
     * Fetch the followers from a user
     * @param twitchAccessToken Requires an app access token or user access token: Bearer token
     * @param userId user id to fetch
     * @param maximumReturned The maximum number of items to return per page
     * @param cursorAfter The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return followers list
     */
    @GetMapping("follows")
    TwitchDataResponseDTO<TwitchFollowDTO> getUserFollowersList(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                @RequestParam(name = "to_id", required = false, defaultValue = "") String userId,
                                                                @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                                @RequestParam(name = "after", required = false, defaultValue = "") String cursorAfter);

    /**
     * Check if a user is following a streamer
     * @param twitchAccessToken Requires an app access token or user access token: Bearer token
     * @param userId viewer id to check
     * @param streamerId streamer id to check
     * @return a single element list if the user is following otherwise not following
     */
    @GetMapping("follows")
    TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                   @RequestParam(name = "from_id", required = false, defaultValue = "") String userId,
                                                                   @RequestParam(name = "to_id", required = false, defaultValue = "") String streamerId);

}
