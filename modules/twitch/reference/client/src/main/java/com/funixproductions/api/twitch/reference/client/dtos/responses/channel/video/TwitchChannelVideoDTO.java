package com.funixproductions.api.twitch.reference.client.dtos.responses.channel.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchChannelVideoDTO {

    /**
     * An ID that identifies the video.
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * The ID of the stream that the video originated from if the video’s type is “archive;” otherwise, null.
     */
    @JsonProperty(value = "stream_id")
    private String streamId;

    /**
     * The ID of the broadcaster that owns the video.
     */
    @JsonProperty(value = "user_id")
    private String userId;

    /**
     * The broadcaster’s login name.
     */
    @JsonProperty(value = "user_login")
    private String userLogin;

    /**
     * The broadcaster’s display name.
     */
    @JsonProperty(value = "user_name")
    private String userName;

    /**
     * The video’s title.
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * The video’s description.
     */
    @JsonProperty(value = "description")
    private String description;

    /**
     * 	The date and time, in UTC, of when the video was created. The timestamp is in RFC3339 format.
     */
    @JsonProperty(value = "created_at")
    private Date createdAt;

    /**
     * The date and time, in UTC, of when the video was published. The timestamp is in RFC3339 format.
     */
    @JsonProperty(value = "published_at")
    private Date publishedAt;

    /**
     * The video’s URL.
     */
    @JsonProperty(value = "url")
    private String url;

    /**
     * A URL to a thumbnail image of the video.
     */
    @JsonProperty(value = "thumbnail_url")
    private String thumbnailUrl;

    /**
     * The number of times that users have watched the video.
     */
    @JsonProperty(value = "view_count")
    private Integer views;

    /**
     * The ISO 639-1 two-letter language code that the video was broadcast in. For example, the language code is DE if the video was broadcast in German.
     */
    @JsonProperty(value = "language")
    private String language;

    /**
     * The video’s type. Possible values are: archive, highlight, upload
     */
    @JsonProperty(value = "type")
    private String type;

    /**
     * The video’s length in ISO 8601 duration format. For example, 3m21s represents 3 minutes, 21 seconds.
     */
    @JsonProperty(value = "duration")
    private String duration;

    /**
     * Get thumbnail and replace data
     * @param width width
     * @param height height
     * @return url image
     */
    public String getThumbnailUrl(final int width, final int height) {
        return this.thumbnailUrl.replace("{width}", Integer.toString(width)).replace("{height}", Integer.toString(height));
    }

}
