package com.funixproductions.api.client.twitch.reference.dtos.responses.channel.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelClipDTO {

    /**
     * An ID that uniquely identifies the clip.
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * A URL to the clip.
     */
    @JsonProperty(value = "url")
    private String url;

    /**
     * A URL that you can use in an iframe to embed the clip
     */
    @JsonProperty(value = "embed_url")
    private String embedUrl;

    /**
     * An ID that identifies the broadcaster that the video was clipped from.
     */
    @JsonProperty(value = "broadcaster_id")
    private String broadcasterId;

    /**
     * The broadcaster’s display name.
     */
    @JsonProperty(value = "broadcaster_name")
    private String broadcasterName;

    /**
     * An ID that identifies the user that created the clip.
     */
    @JsonProperty(value = "creator_id")
    private String creatorId;

    /**
     * The user’s display name.
     */
    @JsonProperty(value = "creator_name")
    private String creatorName;

    /**
     * An ID that identifies the video that the clip came from. This field contains an empty string if the video is not available.
     */
    @JsonProperty(value = "video_id")
    private String videoId;

    /**
     * The ID of the game that was being played when the clip was created.
     */
    @JsonProperty(value = "game_id")
    private String gameId;

    /**
     * The ISO 639-1 two-letter language code that the broadcaster broadcasts in.
     * For example, en for English. The value is other if the broadcaster uses a language that Twitch doesn’t support.
     */
    @JsonProperty(value = "language")
    private String language;

    /**
     * The title of the clip.
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * The number of times the clip has been viewed.
     */
    @JsonProperty(value = "view_count")
    private Integer viewCount;

    /**
     * 	The date and time of when the clip was created. The date and time is in RFC3339 format.
     */
    @JsonProperty(value = "created_at")
    private String createdAt;

    /**
     * A URL to a thumbnail image of the clip.
     */
    @JsonProperty(value = "thumbnail_url")
    private String thumbnailUrl;

    /**
     * The length of the clip, in seconds. Precision is 0.1.
     */
    @JsonProperty(value = "duration")
    private Float duration;

    /**
     * The zero-based offset, in seconds, to where the clip starts in the video (VOD).
     * Is null if the video is not available or hasn’t been created yet from the live stream (see video_id).
     * Note that there’s a delay between when a clip is created during a broadcast and when the offset is set.
     * During the delay period, vod_offset is null.
     * The delay is indeterminant but is typically minutes long.
     */
    @JsonProperty(value = "vod_offset")
    private Integer vodOffset;

}
