package com.funixproductions.api.service.user.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSession {

    private final User user;
    private final String ip;

}
