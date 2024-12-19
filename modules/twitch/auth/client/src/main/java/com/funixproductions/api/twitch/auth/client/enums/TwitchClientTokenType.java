package com.funixproductions.api.twitch.auth.client.enums;

import com.google.common.base.Strings;

import javax.annotation.Nullable;

public enum TwitchClientTokenType {
    FUNIXGAMING,
    STREAMER,
    MODERATOR,
    BOT,
    VIEWER;

    public static TwitchClientTokenType getTokenTypeByString(@Nullable final String tokenType) {
        if (Strings.isNullOrEmpty(tokenType)) {
            return TwitchClientTokenType.VIEWER;
        }

        for (final TwitchClientTokenType token : TwitchClientTokenType.values()) {
            if (token.name().equalsIgnoreCase(tokenType)) {
                return token;
            }
        }

        return TwitchClientTokenType.VIEWER;
    }
}
