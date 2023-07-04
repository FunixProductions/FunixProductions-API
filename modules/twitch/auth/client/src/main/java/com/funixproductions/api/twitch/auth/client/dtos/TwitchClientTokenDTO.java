package com.funixproductions.api.twitch.auth.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class TwitchClientTokenDTO extends ApiDTO {
    private UUID userUuid;

    private String twitchUserId;

    private String twitchUsername;

    private String accessToken;

    private Date expirationDateToken;

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
