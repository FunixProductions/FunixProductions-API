package com.funixproductions.api.twitch.auth.service.resources;

import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.api.twitch.auth.client.clients.TwitchAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.auth.service.configurations.TwitchAuthConfig;
import com.funixproductions.api.twitch.auth.service.services.TwitchClientTokenService;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.security.CurrentSession;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiUnauthorizedException;
import com.google.common.base.Strings;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/twitch/auth")
@RequiredArgsConstructor
public class TwitchAuthResource implements TwitchAuthClient {

    private final TwitchClientTokenService twitchClientTokenService;
    private final TwitchAuthConfig twitchAuthConfig;
    private final CurrentSession currentSession;

    @Override
    public String getAuthClientUrl(@NonNull String tokenType, @Nullable String origin) {
        return this.twitchClientTokenService.getAuthClientUrl(
                TwitchClientTokenType.getTokenTypeByString(tokenType),
                FrontOrigins.getRedirectAuthOrigin(origin)
        );
    }

    @Override
    public TwitchClientTokenDTO getAccessToken(@NonNull String tokenType) {
        final UserDTO actualUser = currentSession.getCurrentUser();

        if (actualUser == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté à l'api.");
        } else {
            return this.twitchClientTokenService.fetchToken(
                    actualUser.getId(),
                    TwitchClientTokenType.getTokenTypeByString(tokenType)
            );
        }
    }

    @GetMapping("cb")
    public void authClientCallback(@RequestParam(required = false) String code,
                                   @RequestParam(required = false) String state,
                                   @RequestParam(required = false) String error,
                                   @RequestParam(required = false, name = "error_description") String errorMessage,
                                   HttpServletResponse response) {
        if (!Strings.isNullOrEmpty(error) || !Strings.isNullOrEmpty(errorMessage)) {
            throw new ApiUnauthorizedException("Une erreur Twitch est survenue. Veuillez vérifier que vous avez autorisé l'application FunixProductions API sur Twitch.");
        } else {
            final FrontOrigins origins = this.twitchClientTokenService.registerNewAuthorizationAuthToken(code, state);

            try {
                if (origins != null) {
                    response.sendRedirect(String.format(
                            "%s/callbacks/twitch",
                            Boolean.TRUE.equals(this.twitchAuthConfig.getDevMode()) ? origins.getDomainDev() : origins.getDomainProd()
                    ));
                } else {
                    response.getWriter().write(
                            "Votre compte est connecté avec Twitch ! Vous pouvez fermer cette fenêtre."
                    );
                }
            } catch (Exception e) {
                throw new ApiException("Erreur interne lors de la redirection après la connexion avec Twitch. Veuillez recommencer.", e);
            }
        }
    }

}
