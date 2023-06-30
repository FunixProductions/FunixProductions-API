package com.funixproductions.api.twitch.reference.service.clients.video;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.video.TwitchChannelVideoDTO;
import com.funixproductions.api.twitch.reference.service.clients.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchReferenceVideosClient",
        url = "https://api.twitch.tv",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/videos"
)
public interface TwitchReferenceVideosClient {

    @GetMapping
    TwitchDataResponseDTO<TwitchChannelVideoDTO> getStreamerVods(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                 @RequestParam(name = "user_id") String streamerId,
                                                                 @RequestParam(name = "first", required = false, defaultValue = "20") Integer amountReturned,
                                                                 @RequestParam(name = "before", required = false, defaultValue = "") String before,
                                                                 @RequestParam(name = "after", required = false, defaultValue = "") String after);

}
