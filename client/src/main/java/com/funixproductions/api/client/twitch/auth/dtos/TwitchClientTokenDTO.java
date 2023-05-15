package com.funixproductions.api.client.twitch.auth.dtos;

import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchClientTokenDTO extends ApiDTO {
    private UserDTO user;

    private String twitchUserId;

    private String twitchUsername;

    private String accessToken;

    private Date expirationDateToken;
}
