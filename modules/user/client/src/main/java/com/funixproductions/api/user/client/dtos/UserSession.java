package com.funixproductions.api.user.client.dtos;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSession {

    private final UserDTO userDTO;

    private final String ip;

    private final HttpServletRequest servletRequest;

}
