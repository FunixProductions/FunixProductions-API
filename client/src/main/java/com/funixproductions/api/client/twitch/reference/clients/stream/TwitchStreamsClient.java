package com.funixproductions.api.client.twitch.reference.clients.stream;

import com.funixproductions.api.client.core.config.FeignConfig;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.stream.TwitchStreamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchStreamsClient",
        url = "${funixproductions.api.app-domain-url}",
        path = "twitch/streams",
        configuration = FeignConfig.class
)
public interface TwitchStreamsClient {

    /**
     * Gets a list of all broadcasters that are streaming.
     * The list is in descending order by the number of viewers watching the stream.
     * Because viewers come and go during a stream,it’s possible to find duplicate or missing streams in the list as you page through the results.
     * @param streamerName 	A user login name used to filter the list of streams. Returns only the streams of those users that are broadcasting.
     *                      You may specify a maximum of 100 login names. To specify multiple names, include the user_login parameter for each user.
     *                      For example, user_login=foo user_login=bar.
     * @return list streams
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchStreamDTO> getStreams(@RequestParam(name = "user_login") String streamerName);

    /**
     * Fetch the funixgaming stream status
     * @return stream status
     */
    @GetMapping("funix")
    TwitchDataResponseDTO<TwitchStreamDTO> getFunixStream();
}
