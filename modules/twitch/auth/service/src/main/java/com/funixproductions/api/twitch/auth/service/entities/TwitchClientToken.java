package com.funixproductions.api.twitch.auth.service.entities;

import com.funixproductions.api.encryption.client.utils.EncryptionString;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "twitch_client_tokens")
public class TwitchClientToken extends ApiEntity {

    @Column(name = "user_uuid", nullable = false, unique = true)
    private String userUuid;

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

    @Column(name = "token_type", nullable = false)
    private TwitchClientTokenType tokenType;

    public boolean isUsable() {
        return Instant.now().isBefore(expirationDateToken.toInstant());
    }

    public UUID getUserUuid() {
        if (this.userUuid == null) {
            return null;
        }

        return UUID.fromString(userUuid);
    }

    public void setUserUuid(UUID userUuid) {
        if (userUuid == null) {
            this.userUuid = null;
        } else {
            this.userUuid = userUuid.toString();
        }
    }
}
