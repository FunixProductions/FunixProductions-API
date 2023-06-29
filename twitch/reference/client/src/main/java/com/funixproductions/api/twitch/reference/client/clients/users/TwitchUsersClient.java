package com.funixproductions.api.twitch.reference.client.clients.users;

import com.funixproductions.api.client.core.config.FeignConfig;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.user.TwitchFollowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchUsersClient",
        url = "${funixproductions.api.app-domain-url}",
        path = "twitch/users",
        configuration = FeignConfig.class
)
public interface TwitchUsersClient {

    /**
     * Check if a user is following a streamer
     * @param userId viewer id to check
     * @param streamerId streamer id to check
     * @return a single element list if the user is following otherwise not following
     */
    @GetMapping("is_following")
    TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(@RequestParam(name = "user_id") String userId,
                                                                   @RequestParam(name = "streamer_id") String streamerId);

}
