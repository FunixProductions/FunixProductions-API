package com.funixproductions.api.twitch.eventsub.service.enums;

import com.funixproductions.api.twitch.eventsub.service.requests.channel.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChannelEventType {
    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelfollow">Doc</a>
     */
    FOLLOW("channel.follow", "2", ChannelFollowSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelupdate">Doc</a>
     */
    UPDATE("channel.update", "2", ChannelUpdateSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscribe">Doc</a>
     */
    SUBSCRIPTION("channel.subscribe", "1", ChannelSubSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptiongift">Doc</a>
     */
    SUB_GIFT("channel.subscription.gift", "1", ChannelSubGiftSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptionmessage">Doc</a>
     */
    RESUB_MESSAGE("channel.subscription.message", "1", ChannelResubMessageSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelraid">Doc</a>
     */
    RAID_RECEIVED("channel.raid", "1", ChannelRaidReceivedSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelcheer">Doc</a>
     */
    CHEER("channel.cheer", "1", ChannelCheerSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelban">Doc</a>
     */
    BAN("channel.ban", "1", ChannelBanSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelchannel_points_custom_reward_redemptionadd">Doc</a>
     */
    POINTS_REWARD_GET("channel.channel_points_custom_reward_redemption.add", "1", ChannelRewardGetSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpollbegin">Doc</a>
     */
    POLL_BEGIN("channel.poll.begin", "1", ChannelPollBeginSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpollprogress">Doc</a>
     */
    POLL_PROGRESS("channel.poll.progress", "1", ChannelPollProgressSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpollend">Doc</a>
     */
    POLL_END("channel.poll.end", "1", ChannelPollEndSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpredictionbegin">Doc</a>
     */
    PREDICTION_BEGIN("channel.prediction.begin", "1", ChannelPredictionBeginSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpredictionprogress">Doc</a>
     */
    PREDICTION_PROGRESS("channel.prediction.progress", "1", ChannelPredictionProgressSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpredictionend">Doc</a>
     */
    PREDICTION_END("channel.prediction.end", "1", ChannelPredictionEndSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelhype_trainbegin">Doc</a>
     */
    HYPE_TRAIN_BEGIN("channel.hype_train.begin", "1", ChannelHypeTrainBeginSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelhype_trainprogress">Doc</a>
     */
    HYPE_TRAIN_PROGRESS("channel.hype_train.progress", "1", ChannelHypeTrainProgressSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelhype_trainend">Doc</a>
     */
    HYPE_TRAIN_END("channel.hype_train.end", "1", ChannelHypeTrainEndSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelshoutoutcreate">Doc</a>
     */
    SHOUTOUT_CREATE("channel.shoutout.create", "1", ChannelShoutOutCreateSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelshoutoutreceive">Doc</a>
     */
    SHOUTOUT_RECEIVE("channel.shoutout.receive", "1", ChannelShoutOutReceiveSubscription.class);

    private final String type;
    private final String version;
    private final Class<? extends AChannelSubscription> clazz;

}
