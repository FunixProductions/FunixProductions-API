package com.funixproductions.api.service.twitch.auth.resources;

import com.funixproductions.api.client.twitch.auth.clients.TwitchAuthClient;
import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.auth.enums.TwitchClientTokenType;
import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.user.services.CurrentSession;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/auth")
@RequiredArgsConstructor
public class TwitchAuthResource implements TwitchAuthClient {

    private final TwitchClientTokenService twitchClientTokenService;
    private final CurrentSession currentSession;

    @Override
    public String getAuthClientUrl(String tokenType) {
        return this.twitchClientTokenService.getAuthClientUrl(getTokenTypeByString(tokenType));
    }

    @Override
    public TwitchClientTokenDTO getAccessToken() {
        final UserDTO actualUser = currentSession.getCurrentUser();

        if (actualUser == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté à l'api.");
        } else {
            return this.twitchClientTokenService.fetchToken(actualUser.getId());
        }
    }

    @GetMapping("cb")
    public String authClientCallback(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String state,
                                     @RequestParam(required = false) String error,
                                     @RequestParam(required = false, name = "error_description") String errorMessage) {
        if (!Strings.isNullOrEmpty(error) || !Strings.isNullOrEmpty(errorMessage)) {
            return "Une erreur Twitch est survenue. Veuillez vérifier que vous avez autorisé l'application FunixAPI sur Twitch.";
        } else {
            this.twitchClientTokenService.registerNewAuthorizationAuthToken(code, state);
            return "Votre compte est connecté avec Twitch ! Vous pouvez fermer cette fenêtre.";
        }
    }

    private TwitchClientTokenType getTokenTypeByString(final String tokenType) {
        for (final TwitchClientTokenType token : TwitchClientTokenType.values()) {
            if (token.name().equalsIgnoreCase(tokenType)) {
                return token;
            }
        }

        return TwitchClientTokenType.VIEWER;
    }
}
