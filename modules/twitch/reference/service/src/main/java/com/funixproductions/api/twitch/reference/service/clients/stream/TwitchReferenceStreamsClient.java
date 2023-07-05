package com.funixproductions.api.twitch.reference.service.clients.stream;

import com.funixproductions.api.twitch.auth.client.configurations.TwitchApiRequestInterceptor;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchReferenceStreamsClient",
        url = "https://api.twitch.tv",
        configuration = TwitchApiRequestInterceptor.class,
        path = "helix/streams"
)
public interface TwitchReferenceStreamsClient {

    /**
     * Gets a list of all broadcasters that are streaming.
     * The list is in descending order by the number of viewers watching the stream.
     * Because viewers come and go during a stream,it’s possible to find duplicate or missing streams in the list as you page through the results.
     * @param twitchAccessToken Requires an app access token or user access token. Bearer {accessToken}
     * @param streamerName 	A user login name used to filter the list of streams. Returns only the streams of those users that are broadcasting.
     *                      You may specify a maximum of 100 login names. To specify multiple names, include the user_login parameter for each user.
     *                      For example, user_login=foo user_login=bar.
     * @return list streams
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchStreamDTO> getStreams(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                      @RequestParam(name = "user_login") String streamerName);

}
