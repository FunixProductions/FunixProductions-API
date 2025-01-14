package com.funixproductions.api.twitch.auth.client.dtos;

import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TwitchClientTokenDTO extends ApiDTO {

    private UUID userUuid;

    private String twitchUserId;

    private String twitchUsername;

    private String accessToken;

    private List<String> scopes;

    private Date expirationDateToken;

    private TwitchClientTokenType tokenType;

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
