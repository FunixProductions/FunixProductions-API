package com.funixproductions.api.twitch.eventsub.client.clients;

import com.funixproductions.api.twitch.eventsub.client.dtos.TwitchEventSubListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "TwitchEventSubClient",
        url = "${funixproductions.api.app-domain-url}",
        path = "/twitch/eventsub/"
)
public interface TwitchEventSubClient {

    /**
     * Get subscriptions
     * <a href="https://dev.twitch.tv/docs/api/reference/#get-eventsub-subscriptions">Doc</a>
     * @param status <p>Filter subscriptions by its status. Possible values are:<br>
     *               enabled — The subscription is enabled.<br>
     *               notification_failures_exceeded — The notification delivery failure rate was too high.<br>
     *               authorization_revoked — The authorization was revoked for one or more users specified in the Condition object.<br>
     *               moderator_removed — The moderator that authorized the subscription is no longer one of the broadcaster's moderators.<br>
     *               user_removed — One of the users specified in the Condition object was removed.</p>
     * @param type Filter subscriptions by subscription type. For a list of subscription types, see <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types#subscription-types">Subscription Types</a>.
     * @param userId Filter subscriptions by user ID. The response contains subscriptions where this ID matches a user ID that you specified in the Condition object when you created the subscription.
     * @param after The cursor used to get the next page of results. The pagination object in the response contains the cursor’s value.
     * @return sub list
     */
    @GetMapping
    TwitchEventSubListDTO getSubscriptions(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "user_id", required = false) String userId,
            @RequestParam(name = "after", required = false) String after
    );

    /**
     * Documentation <a href="https://dev.twitch.tv/docs/api/reference/#delete-eventsub-subscription">Doc remove</a>
     * @param streamerUsername streamer username who has created streamer token with funix api
     */
    @DeleteMapping
    void deleteSubscription(@RequestParam(name = "streamer_username") String streamerUsername);

    /**
     * Documentation <a href="https://dev.twitch.tv/docs/api/reference/#create-eventsub-subscription">Create sub</a>
     * @param streamerUsername streamer username who has created streamer token with funix api
     */
    @PostMapping
    void createSubscription(@RequestBody String streamerUsername);

}
