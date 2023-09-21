package com.funixproductions.api.twitch.auth.service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Token dto app token
 * <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#client-credentials-grant-flow">Documentation</a>
 */
@Getter
@Setter
public class TwitchTokenResponseDTO {

    /**
     * Access token twitch app server to server
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * Refresh token to get a new access token, only available on client token not server
     */
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    /**
     * Time in seconds when the token will expire
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "scope")
    private List<String> scopes;

    /**
     * Token type, bearer
     */
    @JsonProperty(value = "token_type")
    private String tokenType;

}
