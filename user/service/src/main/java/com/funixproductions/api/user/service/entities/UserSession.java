package com.funixproductions.api.user.service.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSession {

    private final User user;
    private final String bearerToken;
    private final String ip;

}
