package com.funixproductions.api.service.twitch.eventsub.dtos.requests;

import com.google.gson.JsonObject;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-reference/">Documentation</a>
 */
public abstract class TwitchSubscription {

    /**
     * The type of subscription to create. For a list of subscriptions that you can create,
     * see <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#subscription-types">Subscription types</a>.<br>
     * Set this field to the value in the Name column of the Subscription Types table.
     */
    private final String type;

    /**
     * 	The version number that identifies the definition of the subscription type that you want the response to use.
     */
    private final String version;

    /**
     * Shared hmac encryption key
     */
    private String secretHmacKey;

    /**
     * Callback url
     */
    private String eventUrlCallback;

    /**
     * Constructor for base twitch subscription body
     * @param type type see <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types#subscription-types">Subscription Types</a>.
     * @param version The version number that identifies this definition of the subscription’s data.
     */
    protected TwitchSubscription(final String type,
                              final String version) {
        this.type = type;
        this.version = version;
    }

    /**
     * Method used to define the condition of the event
     * A JSON object that contains the parameter values that are specific to the specified subscription type.
     * For the object’s required and optional fields, see the subscription type’s documentation.
     * @return JsonObject used in append at final method getPayload
     */
    protected abstract JsonObject getCondition();

    /**
     * Static method used to build transport object
     * @return json object
     */
    private JsonObject getTransport() {
        final JsonObject transport = new JsonObject();

        transport.addProperty("method", "webhook");
        transport.addProperty("callback", this.eventUrlCallback);
        transport.addProperty("secret", this.secretHmacKey);
        return transport;
    }

    /**
     * Build the payload to send to twitch
     * @return String body to send
     */
    public final String getPayload() {
        final JsonObject body = new JsonObject();

        body.addProperty("type", this.type);
        body.addProperty("version", this.version);
        body.add("condition", getCondition());
        body.add("transport", getTransport());
        return body.toString();
    }

    public final void setSecretHmacKey(final String hmacKey) {
        this.secretHmacKey = hmacKey;
    }

    public final void setEventUrlCallback(final String urlCallback) {
        this.eventUrlCallback = urlCallback;
    }

    public final String getType() {
        return this.type;
    }

}
