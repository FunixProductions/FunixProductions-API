package com.funixproductions.api.client.twitch.reference.dtos.responses.channel.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelClipCreationDTO {

    /**
     * A URL that you can use to edit the clip’s title, identify the part of the clip to publish, and publish the clip.
     */
    @JsonProperty(value = "edit_url")
    private String editUrl;

    /**
     * An ID that uniquely identifies the clip.
     */
    @JsonProperty(value = "id")
    private String id;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final TwitchChannelClipCreationDTO ent) {
            return ent.getId().equals(this.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
