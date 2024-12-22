package com.funixproductions.api.twitch.eventsub.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/manage-subscriptions/#getting-the-list-of-events-you-subscribe-to">Doc</a>
 */
@Getter
@AllArgsConstructor
public enum TwitchEventStatus {
    /**
     * Twitch has verified your callback and is able to send you notifications.
     */
    ENABLED("enabled"),
    /**
     * Twitch is verifying that you own the callback specified in the create subscription request. For information about how it does this, see <a href="https://dev.twitch.tv/docs/eventsub/handling-webhook-events/#responding-to-a-challenge-request">Verifying your callback</a>. Used only for webhook subscriptions.
     */
    CALLBACK_VERIFICATION_PENDING("webhook_callback_verification_pending"),
    /**
     * witch failed to verify that you own the callback specified in the create subscription request. Fix your event handler to correctly respond to the challenge, and then try subscribing again. Used only for webhook subscriptions.
     */
    CALLBACK_VERIFICATION_FAILED("webhook_callback_verification_failed"),
    /**
     * Twitch revoked your subscription because the notification delivery failure rate was too high. Used only for webhook subscriptions.
     */
    NOTIFICATION_FAILURES_EXCEEDED("notification_failures_exceeded"),
    /**
     * Twitch revoked your subscription because the users in the condition object revoked their authorization letting you get events on their behalf, or changed their password.
     */
    AUTHORIZATION_REVOKED("authorization_revoked"),
    /**
     * The moderator that authorized the subscription is no longer one of the broadcaster’s moderators.
     */
    MODERATOR_REMOVED("moderator_removed"),
    /**
     * Twitch revoked your subscription because the users in the condition object are no longer Twitch users.
     */
    USER_REMOVED("user_removed"),
    /**
     * Twitch revoked your subscription because the subscription to subscription type and version is no longer supported.
     */
    VERSION_REMOVED("version_removed"),
    /**
     * Twitch revoked your subscription because the beta subscription type was undergoing maintenance.
     */
    BETA_MAINTENANCE("beta_maintenance");

    private final String status;
}
