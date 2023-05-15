package com.funixproductions.api.service.twitch.auth.entities;

import com.funixproductions.api.service.core.encryption.EncryptionString;
import com.funixproductions.api.service.user.entities.User;
import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity(name = "twitch_client_tokens")
public class TwitchClientToken extends ApiEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Convert(converter = EncryptionString.class)
    @Column(name = "twitch_user_id", nullable = false)
    private String twitchUserId;

    @Convert(converter = EncryptionString.class)
    @Column(name = "twitch_username", nullable = false)
    private String twitchUsername;

    @Convert(converter = EncryptionString.class)
    @Column(name = "o_auth_code", nullable = false, unique = true)
    private String oAuthCode;

    @Convert(converter = EncryptionString.class)
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Convert(converter = EncryptionString.class)
    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "expiration_date_token", nullable = false)
    private Date expirationDateToken;

    public boolean isUsable() {
        return Instant.now().isBefore(expirationDateToken.toInstant());
    }
}
