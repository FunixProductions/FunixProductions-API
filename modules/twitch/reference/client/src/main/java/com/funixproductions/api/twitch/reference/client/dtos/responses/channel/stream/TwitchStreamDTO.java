package com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchStreamDTO {

    /**
     * An ID that identifies the stream. You can use this ID later to look up the video on demand (VOD).
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * The ID of the user that’s broadcasting the stream.
     */
    @JsonProperty(value = "user_id")
    private String userId;

    /**
     * The user’s login name.
     */
    @JsonProperty(value = "user_login")
    private String userLogin;

    /**
     * The user’s display name.
     */
    @JsonProperty(value = "user_name")
    private String userName;

    /**
     * The ID of the category or game being played.
     */
    @JsonProperty(value = "game_id")
    private String gameId;

    /**
     * The name of the category or game being played.
     */
    @JsonProperty(value = "game_name")
    private String gameName;

    /**
     * The stream’s title. Is an empty string if not set.
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * The number of users watching the stream.
     */
    @JsonProperty(value = "viewer_count")
    private Integer viewerCount;

    /**
     * The UTC date and time (in RFC3339 format) of when the broadcast began.
     */
    @JsonProperty(value = "started_at")
    private Date startedAt;

    /**
     * The language that the stream uses. This is an ISO 639-1 two-letter language code
     */
    @JsonProperty(value = "language")
    private String language;

    /**
     * 	A URL to an image of a frame from the last 5 minutes of the stream.
     * 	Replace the width and height placeholders in the URL ({width}x{height}) with the size of the image you want, in pixels.
     */
    @JsonProperty(value = "thumbnail_url")
    private String thumbnailUrl;

    /**
     * A Boolean value that indicates whether the stream is meant for mature audiences.
     */
    @JsonProperty(value = "is_mature")
    private Boolean isMature;

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
