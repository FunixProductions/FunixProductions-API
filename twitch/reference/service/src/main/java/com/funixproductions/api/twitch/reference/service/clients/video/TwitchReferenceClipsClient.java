package com.funixproductions.api.twitch.reference.service.clients.video;

import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.video.TwitchChannelClipCreationDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.channel.video.TwitchChannelClipDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(
        name = "TwitchReferenceClipsClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/clips"
)
public interface TwitchReferenceClipsClient {

    /**
     * Creates a clip from the broadcaster’s stream.
     * Requires a user access token that includes the clips:edit scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose stream you want to create a clip from.
     * @return clip creation instance with url and id to edit
     */
    @PostMapping
    TwitchChannelClipCreationDTO createClip(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                            @RequestParam(name = "broadcaster_id") String broadcasterId);

    /**
     * Gets one or more video clips that were captured from streams. For information about clips, see How to use clips.
     * Requires an app access token or user access token.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId An ID that identifies the broadcaster whose video clips you want to get.
     *                      Use this parameter to get clips that were captured from the broadcaster’s streams.
     * @param startedAt	The start date used to filter clips. The API returns only clips within the start and end date window.
     *                  Specify the date and time in RFC3339 format.
     * @param endedAt The end date used to filter clips. If not specified, the time window is the start date plus one week.
     *                Specify the date and time in RFC3339 format.
     * @param amountReturned The maximum number of clips to return per page in the response.
     *                       The minimum page size is 1 clip per page and the maximum is 100.The default is 20.
     * @param before The cursor used to get the previous page of results. The Pagination object in the response contains the cursor’s value.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return clip list
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchChannelClipDTO> getStreamerClips(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                 @RequestParam(name = "broadcaster_id") String broadcasterId,
                                                                 @RequestParam(name = "started_at", required = false, defaultValue = "") Date startedAt,
                                                                 @RequestParam(name = "ended_at", required = false, defaultValue = "") Date endedAt,
                                                                 @RequestParam(name = "first", required = false, defaultValue = "20") Integer amountReturned,
                                                                 @RequestParam(name = "before", required = false, defaultValue = "") String before,
                                                                 @RequestParam(name = "after", required = false, defaultValue = "") String after);

    /**
     * Gets one or more video clips that were captured from streams. For information about clips, see How to use clips.
     * Requires an app access token or user access token.
     * @param twitchAccessToken Bearer {accessToken}
     * @param id An ID that identifies the clip to get. To specify more than one ID, include this parameter for each clip you want to get. For example,
     *           id=foo id=bar. You may specify a maximum of 100 IDs. The API ignores duplicate IDs and IDs that aren’t found.
     * @param startedAt	The start date used to filter clips. The API returns only clips within the start and end date window.
     *                  Specify the date and time in RFC3339 format.
     * @param endedAt The end date used to filter clips. If not specified, the time window is the start date plus one week.
     *                Specify the date and time in RFC3339 format.
     * @param amountReturned The maximum number of clips to return per page in the response.
     *                       The minimum page size is 1 clip per page and the maximum is 100.The default is 20.
     * @param before The cursor used to get the previous page of results. The Pagination object in the response contains the cursor’s value.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return clip list
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchChannelClipDTO> getClipsById(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                             @RequestParam(name = "started_at", required = false, defaultValue = "") Date startedAt,
                                                             @RequestParam(name = "ended_at", required = false, defaultValue = "") Date endedAt,
                                                             @RequestParam(name = "first", required = false, defaultValue = "20") Integer amountReturned,
                                                             @RequestParam(name = "before", required = false, defaultValue = "") String before,
                                                             @RequestParam(name = "after", required = false, defaultValue = "") String after,
                                                             @RequestParam(name = "id") List<String> id);

}
