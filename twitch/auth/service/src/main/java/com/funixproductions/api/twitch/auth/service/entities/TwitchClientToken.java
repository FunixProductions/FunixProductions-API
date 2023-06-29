package com.funixproductions.api.twitch.auth.service.entities;

import com.funixproductions.api.encryption.client.utils.EncryptionString;
import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "twitch_client_tokens")
public class TwitchClientToken extends ApiEntity {

    @Column(name = "user_uuid", nullable = false, unique = true)
    private UUID userUuid;

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

    public String getUserUuid() {
        if (this.userUuid == null) {
            return null;
        }

        return userUuid.toString();
    }

    public void setUserUuid(String userUuid) {
        if (Strings.isBlank(userUuid)) {
            this.userUuid = null;
        } else {
            this.userUuid = UUID.fromString(userUuid);
        }
    }
}
