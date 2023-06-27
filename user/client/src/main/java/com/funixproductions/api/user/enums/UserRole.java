package com.funixproductions.api.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("USER"),
    MODERATOR("MODERATOR"),
    PACIFISTA_MODERATOR("PACIFISTA_MODERATOR"),
    PACIFISTA_ADMIN("PACIFISTA_ADMIN"),
    ADMIN("ADMIN");

    private final String role;
}
