package com.funixproductions.api.user.client.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSession {

    private final UserDTO userDTO;

    private final String ip;

}
