package com.funixproductions.api.twitch.auth.client.enums;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public enum TwitchClientTokenType {
    FUNIXGAMING(true),
    STREAMER(true),
    VIEWER(false);

    private boolean needRegisterEvents;

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
