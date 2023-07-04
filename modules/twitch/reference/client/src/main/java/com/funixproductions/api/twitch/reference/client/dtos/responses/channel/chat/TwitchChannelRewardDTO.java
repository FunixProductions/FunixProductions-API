package com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.twitch.reference.client.dtos.responses.common.TwitchImageDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelRewardDTO {

    @Getter
    @Setter
    public static class MaxPerStreamSetting {
        /**
         * A Boolean value that determines whether the reward applies a limit on the number of
         * redemptions allowed per live stream. Is true if the reward applies a limit.
         */
        @JsonProperty(value = "is_enabled")
        private Boolean isEnabled;

        /**
         * The maximum number of redemptions allowed per live stream.
         */
        @JsonProperty(value = "max_per_stream")
        private Integer maxPerStream;
    }

    @Getter
    @Setter
    public static class MaxPerUserPerStreamSetting {
        /**
         * A Boolean value that determines whether the reward applies a limit on the number of
         * redemptions allowed per live stream. Is true if the reward applies a limit.
         */
        @JsonProperty(value = "is_enabled")
        private Boolean isEnabled;

        /**
         * A Boolean value that determines whether the reward applies a limit on the number
         * of redemptions allowed per user per live stream. Is true if the reward applies a limit.
         */
        @JsonProperty(value = "max_per_user_per_stream")
        private Integer maxPerUserPerStream;
    }

    @Getter
    @Setter
    public static class GlobalCooldownSetting {
        /**
         * A Boolean value that determines whether to apply a cooldown period. Is true if a cooldown period is enabled.
         */
        @JsonProperty(value = "is_enabled")
        private Boolean isEnabled;

        /**
         * The cooldown period, in seconds.
         */
        @JsonProperty(value = "global_cooldown_seconds")
        private Integer globalCooldownSeconds;
    }

    /**
     * The ID that uniquely identifies the broadcaster.
     */
    @JsonProperty(value = "broadcaster_id")
    private String broadcasterId;

    /**
     * The broadcaster’s login name.
     */
    @JsonProperty(value = "broadcaster_login")
    private String broadcasterLogin;

    /**
     * The broadcaster’s display name.
     */
    @JsonProperty(value = "broadcaster_name")
    private String broadcasterName;

    /**
     * The ID that uniquely identifies this custom reward.
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * The title of the reward.
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * The prompt shown to the viewer when they redeem the reward if user input is required
     * (see the is_user_input_required field).
     */
    @JsonProperty(value = "prompt")
    private String prompt;

    /**
     * The cost of the reward in Channel Points.
     */
    @JsonProperty(value = "cost")
    private Integer cost;

    /**
     * 	A set of custom images for the reward.
     * 	This field is null if the broadcaster didn’t upload images.
     */
    @JsonProperty(value = "cost")
    private TwitchImageDTO image;

    /**
     * 	A set of default images for the reward.
     */
    @JsonProperty(value = "default_image")
    private TwitchImageDTO defaultImage;

    /**
     * The background color to use for the reward.
     * The color is in Hex format (for example, #00E5CB).
     */
    @JsonProperty(value = "background_color")
    private String backgroundColor;

    /**
     * A Boolean value that determines whether the reward is enabled. Is true if enabled; otherwise, false. Disabled rewards aren’t shown to the user.
     */
    @JsonProperty(value = "is_enabled")
    private Boolean isEnabled;

    /**
     * 	A Boolean value that determines whether the user must enter information when redeeming the reward. Is true if the user is prompted.
     */
    @JsonProperty(value = "is_user_input_required")
    private Boolean isUserInputRequired;

    /**
     * The settings used to determine whether to apply a maximum to the number of redemptions allowed per user per live stream.
     */
    @JsonProperty(value = "max_per_stream_setting")
    private MaxPerStreamSetting maxUsagePerStream;

    /**
     * The settings used to determine whether to apply a cooldown period between redemptions and the length of the cooldown.
     */
    @JsonProperty(value = "global_cooldown_setting")
    private GlobalCooldownSetting globalCooldownSetting;

    /**
     * 	A Boolean value that determines whether the reward is currently paused.
     * 	Is true if the reward is paused. Viewers can’t redeem paused rewards.
     */
    @JsonProperty(value = "is_paused")
    private Boolean isPaused;

    /**
     * 	A Boolean value that determines whether the reward is currently in stock.
     * 	Is true if the reward is in stock. Viewers can’t redeem out of stock rewards.
     */
    @JsonProperty(value = "is_in_stock")
    private Boolean isInStock;

    /**
     * 	A Boolean value that determines whether redemptions should be set to FULFILLED status
     * 	immediately when a reward is redeemed. If false, status is set to UNFULFILLED and follows
     * 	the normal request queue process.
     */
    @JsonProperty(value = "should_redemptions_skip_request_queue")
    private Boolean skipRequestQueue;

    /**
     * The number of redemptions redeemed during the current live stream.
     * The number counts against the max_per_stream_setting limit.
     * This field is null if the broadcaster’s stream isn’t live or max_per_stream_setting isn’t enabled.
     */
    @JsonProperty(value = "redemptions_redeemed_current_stream")
    private Integer currentStreamRedemptionsCount;

    /**
     * The timestamp of when the cooldown period expires.
     * Is null if the reward isn’t in a cooldown state. See the global_cooldown_setting field.
     */
    @JsonProperty(value = "cooldown_expires_at")
    private String cooldownExpiresAt;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final TwitchChannelRewardDTO ent) {
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
