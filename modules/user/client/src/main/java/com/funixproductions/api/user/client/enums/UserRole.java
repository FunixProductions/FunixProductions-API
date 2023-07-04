package com.funixproductions.api.user.client.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("USER", new String[]{"USER"}),

    MODERATOR("MODERATOR", new String[]{
            "USER",
            "MODERATOR"
    }),

    PACIFISTA_MODERATOR("PACIFISTA_MODERATOR", new String[]{
            "USER",
            "PACIFISTA_MODERATOR"
    }),

    PACIFISTA_ADMIN("PACIFISTA_ADMIN", new String[]{
            "USER",
            "PACIFISTA_ADMIN"
    }),

    ADMIN("ADMIN", new String[]{
            "USER",
            "MODERATOR",
            "PACIFISTA_MODERATOR",
            "PACIFISTA_ADMIN",
            "ADMIN"
    });

    private final String role;

    private final String[] authorities;
}
