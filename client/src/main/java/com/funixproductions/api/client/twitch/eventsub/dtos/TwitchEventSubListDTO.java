package com.funixproductions.api.client.twitch.eventsub.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TwitchEventSubListDTO {

    /**
     * total subscriptions
     */
    private Integer total;

    /**
     * Data list of subscriptions
     */
    private List<TwitchEventSub> data;

    /**
     * The sum of all of your subscription costs.
     */
    @JsonProperty(value = "total_cost")
    private Integer totalCost;

    /**
     * The maximum total cost that you’re allowed to incur for all subscriptions that you create.
     */
    @JsonProperty(value = "max_total_cost")
    private Integer maxTotalCost;

    /**
     * An object that contains the cursor used to get the next page of subscriptions.
     * The object is empty if there are no more pages to get.
     * The number of subscriptions returned per page is undertermined.
     */
    private Pagination pagination;

    @Getter
    @Setter
    public static class TwitchEventSub {
        /**
         * An ID that identifies the subscription.
         */
        private String id;

        /**
         * The subscription’s status. The subscriber receives events only for enabled subscriptions. Possible values are:
         * enabled — The subscription is enabled.
         * webhook_callback_verification_pending — The subscription is pending verification of the specified callback URL.
         * webhook_callback_verification_failed — The specified callback URL failed verification.
         * notification_failures_exceeded — The notification delivery failure rate was too high.
         * authorization_revoked — The authorization was revoked for one or more users specified in the Condition object.
         * moderator_removed — The moderator that authorized the subscription is no longer one of the broadcaster's moderators.
         * user_removed — One of the users specified in the Condition object was removed.
         * version_removed — The subscribed to subscription type and version is no longer supported.
         */
        private String status;

        /**
         * The subscription’s type. See <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/">Subscription Types</a>.
         */
        private String type;

        /**
         * The version number that identifies this definition of the subscription’s data.
         */
        private String version;

        /**
         * The subscription’s parameter values. This is a string-encoded JSON object whose contents are determined by the subscription type.
         */
        private Condition condition;

        /**
         * The date and time (in RFC3339 format) of when the subscription was created.
         */
        @JsonProperty(value = "created_at")
        private Date createdAt;

        /**
         * The amount that the subscription counts against your limit.
         */
        private Integer cost;
    }

    @Getter
    @Setter
    public static class Pagination {

        /**
         * The cursor value that you set the after query parameter to.
         */
        private String cursor;
    }

    @Getter
    @Setter
    public static class Condition {
        @JsonProperty(value = "broadcaster_user_id")
        private String streamerId;

        @JsonProperty(value = "moderator_user_id")
        private String moderatorId;

        @JsonProperty(value = "from_broadcaster_user_id")
        private String fromStreamerId;

        @JsonProperty(value = "to_broadcaster_user_id")
        private String toStreamerId;

        @JsonProperty(value = "reward_id")
        private String rewardId;
    }

    /**
     * Returns pagination string if pagination exists null otherwise
     * @return pagination or null
     */
    @Nullable
    public String hasPagination() {
        if (this.pagination != null && !Strings.isNullOrEmpty(this.pagination.cursor)) {
            return this.pagination.cursor;
        } else {
            return null;
        }
    }

}
