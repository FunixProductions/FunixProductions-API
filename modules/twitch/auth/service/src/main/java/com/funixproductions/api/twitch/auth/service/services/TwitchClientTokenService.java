package com.funixproductions.api.twitch.auth.service.services;

import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.api.twitch.auth.client.configurations.TwitchApiConfig;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.auth.service.clients.TwitchTokenAuthClient;
import com.funixproductions.api.twitch.auth.service.clients.TwitchValidTokenClient;
import com.funixproductions.api.twitch.auth.service.configurations.TwitchAuthConfig;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchTokenResponseDTO;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchValidationTokenResponseDTO;
import com.funixproductions.api.twitch.auth.service.entities.TwitchClientToken;
import com.funixproductions.api.twitch.auth.service.mappers.TwitchClientTokenMapper;
import com.funixproductions.api.twitch.auth.service.repositories.TwitchClientTokenRepository;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.security.CurrentSession;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.google.common.base.Strings;
import feign.FeignException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/**
 * <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#authorization-code-grant-flow">Documentation</a>
 */
@Service
@Slf4j(topic = "TwitchAppClientTokenService")
public class TwitchClientTokenService {

    private final TwitchAuthConfig twitchAuthConfig;
    private final TwitchApiConfig twitchApiConfig;
    private final TwitchClientTokenRepository twitchClientTokenRepository;
    private final TwitchClientTokenMapper twitchClientTokenMapper;
    private final TwitchTokenAuthClient twitchTokenAuthClient;
    private final TwitchValidTokenClient validTokenClient;

    private final PasswordGenerator passwordGenerator;
    private final CurrentSession currentSession;

    private final Map<String, CsrfUser> csrfTokens = new HashMap<>();

    @Getter
    @RequiredArgsConstructor
    private static class CsrfUser {
        private final UserDTO user;
        private final TwitchClientTokenType tokenType;
        private final @Nullable FrontOrigins frontOrigin;
        private final Instant createdAt;
    }

    public TwitchClientTokenService(TwitchAuthConfig twitchAuthConfig,
                                    TwitchClientTokenRepository twitchClientTokenRepository,
                                    TwitchClientTokenMapper twitchClientTokenMapper,
                                    TwitchTokenAuthClient twitchTokenAuthClient,
                                    CurrentSession currentSession,
                                    TwitchApiConfig twitchApiConfig,
                                    TwitchValidTokenClient twitchValidTokenClient) {
        this.twitchClientTokenRepository = twitchClientTokenRepository;
        this.twitchAuthConfig = twitchAuthConfig;
        this.twitchTokenAuthClient = twitchTokenAuthClient;
        this.twitchClientTokenMapper = twitchClientTokenMapper;
        this.currentSession = currentSession;
        this.validTokenClient = twitchValidTokenClient;
        this.twitchApiConfig = twitchApiConfig;

        this.passwordGenerator = new PasswordGenerator();
        passwordGenerator.setSpecialCharsAmount(0);
        passwordGenerator.setAlphaDown(20);
        passwordGenerator.setAlphaUpper(20);
        passwordGenerator.setNumbersAmount(20);
    }

    /**
     * Get client url to auth with twitch api
     * @param tokenType token type to determine the scopes permissions
     * @return url login
     */
    public String getAuthClientUrl(final TwitchClientTokenType tokenType, final FrontOrigins frontOrigin) {
        return "https://id.twitch.tv/oauth2/authorize" +
                "?response_type=code" +
                "&client_id=" + twitchApiConfig.getAppClientId() +
                "&redirect_uri=" + twitchAuthConfig.getAppCallbackDomain() + "/twitch/auth/cb" +
                "&scope=" + generateScopesForTokenType(tokenType) +
                "&state=" + generateNewState(tokenType, frontOrigin);
    }

    /**
     * Called when we receive callback from twitch login
     * @param oAuthCode given by twitch
     * @param csrfToken echo back of the csrf we sended
     * @return FrontOrigins si non null, redirige l'utilisateur sur une page web de la FunixProductions
     */
    @Nullable
    public FrontOrigins registerNewAuthorizationAuthToken(final String oAuthCode, final String csrfToken) {
        if (Strings.isNullOrEmpty(oAuthCode)) {
            throw new ApiBadRequestException("Il manque le oAuth code.");
        } else if (Strings.isNullOrEmpty(csrfToken)) {
            throw new ApiBadRequestException("Il manque le csrf token.");
        }

        final CsrfUser csrfUser = this.csrfTokens.get(csrfToken);
        if (csrfUser == null) {
            throw new ApiBadRequestException("Le csrf token est invalide. Veuillez vous reconnecter avec twitch.");
        }

        final UserDTO userDTO = csrfUser.getUser();
        final TwitchClientTokenType tokenType = csrfUser.getTokenType();
        final Optional<TwitchClientToken> searchToken = this.twitchClientTokenRepository.findTwitchClientTokenByUserUuidAndTokenType(
                userDTO.getId().toString(),
                csrfUser.tokenType
        );

        searchToken.ifPresent(this.twitchClientTokenRepository::delete);

        final TwitchClientToken generatedToken = this.generateNewAccessToken(csrfUser, userDTO, oAuthCode);
        if (tokenType.isNeedRegisterEvents()) {
            this.createEventSub(generatedToken.getTwitchUsername());
        }

        this.csrfTokens.remove(csrfToken);
        return csrfUser.getFrontOrigin();
    }

    public TwitchClientTokenDTO fetchToken(final UUID userUuid, final TwitchClientTokenType tokenType) {
        if (userUuid == null) {
            throw new ApiBadRequestException("Pas de user uuid spécifié pour la récupération de tokens twitch.");
        }

        final TwitchClientToken twitchClientToken = this.twitchClientTokenRepository.findTwitchClientTokenByUserUuidAndTokenType(
                userUuid.toString(),
                tokenType
        ).orElseThrow(() -> new ApiNotFoundException(String.format("L'utilisateur %s ne possède pas de tokens twitch de type %s.", userUuid, tokenType)));

        return refreshToken(twitchClientToken);
    }

    public TwitchClientTokenDTO fetchTokenByStreamerUsername(final String streamerUsername, final TwitchClientTokenType tokenType) {
        if (Strings.isNullOrEmpty(streamerUsername)) {
            throw new ApiBadRequestException("Pas de streamer username spécifié pour la récupération de tokens twitch.");
        }

        final Optional<TwitchClientToken> twitchClientToken = this.twitchClientTokenRepository.findTwitchClientTokenByTwitchUsernameAndTokenType(
                streamerUsername,
                tokenType
        );

        if (twitchClientToken.isPresent()) {
            return refreshToken(twitchClientToken.get());
        } else {
            throw new ApiNotFoundException(String.format("Le streamer %s ne possède pas de tokens twitch de type %s.", streamerUsername, tokenType));
        }
    }

    public TwitchClientTokenDTO fetchTokenByStreamerId(final String streamerId, final TwitchClientTokenType tokenType) {
        if (Strings.isNullOrEmpty(streamerId)) {
            throw new ApiBadRequestException("Pas de streamer id spécifié pour la récupération de tokens twitch.");
        }

        final Optional<TwitchClientToken> twitchClientToken = this.twitchClientTokenRepository.findTwitchClientTokenByTwitchUserIdAndTokenType(
                streamerId,
                tokenType
        );

        if (twitchClientToken.isPresent()) {
            return refreshToken(twitchClientToken.get());
        } else {
            throw new ApiNotFoundException(String.format("Le streamer %s ne possède pas de tokens twitch de type %s.", streamerId, tokenType));
        }
    }

    /**
     * <a href="https://dev.twitch.tv/docs/authentication/scopes">Scopes list</a>
     * <p>Encoders: %20 is space and %3A is :</p>
     * @param tokenType token type generated
     * @return scope list http formatted
     */
    private String generateScopesForTokenType(final TwitchClientTokenType tokenType) {
        final Set<String> scopes;

        if (tokenType == TwitchClientTokenType.FUNIXGAMING) {
            scopes = Set.of(
                    "bits:read",
                    "channel:edit:commercial",
                    "channel:manage:broadcast",
                    "channel:read:charity",
                    "channel:manage:moderators",
                    "channel:manage:polls",
                    "channel:manage:predictions",
                    "channel:manage:raids",
                    "channel:manage:redemptions",
                    "channel:manage:schedule",
                    "channel:manage:videos",
                    "channel:read:hype_train",
                    "channel:read:polls",
                    "channel:read:predictions",
                    "channel:read:redemptions",
                    "channel:read:subscriptions",
                    "channel:read:vips",
                    "channel:manage:vips",
                    "clips:edit",
                    "moderation:read",
                    "moderator:read:followers",
                    "moderator:manage:announcements",
                    "moderator:manage:banned_users",
                    "moderator:read:blocked_terms",
                    "moderator:manage:blocked_terms",
                    "moderator:manage:chat_messages",
                    "moderator:read:chat_settings",
                    "moderator:manage:chat_settings",
                    "moderator:read:chatters",
                    "user:manage:blocked_users",
                    "user:read:blocked_users",
                    "user:edit",
                    "user:read:broadcast",
                    "user:manage:chat_color",
                    "user:read:email",
                    "user:read:follows",
                    "user:read:subscriptions",
                    "channel:moderate",
                    "chat:edit",
                    "chat:read",
                    "moderator:read:shoutouts",
                    "moderator:manage:shoutouts"
            );
        } else if (tokenType == TwitchClientTokenType.STREAMER) {
            scopes = Set.of(
                    "user:read:email",
                    "user:read:follows",
                    "user:read:subscriptions",
                    "moderator:read:followers",
                    "moderator:read:chatters",
                    "bits:read",
                    "channel:read:hype_train",
                    "channel:read:polls",
                    "channel:read:predictions",
                    "channel:read:subscriptions",
                    "channel:read:redemptions",
                    "channel:manage:redemptions"
            );
        } else {
            scopes = Set.of(
                    "channel:read:subscriptions",
                    "user:read:email",
                    "user:read:follows",
                    "user:read:subscriptions"
            );
        }

        return URLEncoder.encode(
                String.join(" ", scopes),
                StandardCharsets.UTF_8
        );
    }

    private String generateNewState(final TwitchClientTokenType tokenType,
                                    final @Nullable FrontOrigins frontOrigin) throws ApiBadRequestException {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté à la FunixProductions API.");
        }

        final CsrfUser csrfUser = new CsrfUser(
                userDTO,
                tokenType,
                frontOrigin,
                Instant.now()
        );
        final String state = passwordGenerator.generateRandomPassword();

        this.csrfTokens.put(state, csrfUser);
        return state;
    }

    private TwitchClientToken generateNewAccessToken(final CsrfUser csrfUser, final UserDTO user, final String oAuthToken) {
        final Map<String, String> formData = new HashMap<>();
        formData.put("client_id", twitchApiConfig.getAppClientId());
        formData.put("client_secret", twitchApiConfig.getAppClientSecret());
        formData.put("code", oAuthToken);
        formData.put("grant_type", "authorization_code");
        formData.put("redirect_uri", twitchAuthConfig.getAppCallbackDomain() + "/twitch/auth/cb");

        try {
            final TwitchTokenResponseDTO tokenResponseDTO = twitchTokenAuthClient.getToken(formData);
            final TwitchClientToken twitchClientToken = new TwitchClientToken();

            final TwitchValidationTokenResponseDTO twitchValidationTokenResponseDTO = validTokenClient.makeHttpRequestValidation(tokenResponseDTO.getAccessToken());
            if (twitchValidationTokenResponseDTO == null) {
                throw new ApiForbiddenException(String.format("L'utilisateur %s à retiré l'accès à la FunixAPI sur twitch.", csrfUser.getUser().getUsername()));
            }

            twitchClientToken.setUserUuid(user.getId());
            twitchClientToken.setTwitchUserId(twitchValidationTokenResponseDTO.getTwitchUserId());
            twitchClientToken.setTwitchUsername(twitchValidationTokenResponseDTO.getTwitchUsername());
            twitchClientToken.setOAuthCode(oAuthToken);
            twitchClientToken.setAccessToken(tokenResponseDTO.getAccessToken());
            twitchClientToken.setRefreshToken(tokenResponseDTO.getRefreshToken());
            twitchClientToken.setTokenType(csrfUser.getTokenType());
            twitchClientToken.setExpirationDateToken(Date.from(Instant.now().plusSeconds(tokenResponseDTO.getExpiresIn() - 60L)));

            return this.twitchClientTokenRepository.save(twitchClientToken);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new ApiForbiddenException(String.format("L'utilisateur %s à retiré l'accès à la FunixAPI sur twitch.", csrfUser.getUser().getUsername()));
            } else {
                throw new ApiException("Une erreur est survenue lors de la récupération d'un nouveau access token via Twitch.", e);
            }
        }
    }

    private TwitchClientTokenDTO refreshToken(final TwitchClientToken token) {
        try {
            final List<String> scopes;

            if (token.isUsable()) {
                final TwitchValidationTokenResponseDTO twitchValidationTokenResponseDTO = validTokenClient.makeHttpRequestValidation(token.getAccessToken());

                if (twitchValidationTokenResponseDTO == null) {
                    throw new ApiForbiddenException(String.format("L'utilisateur %s à retiré l'accès à la FunixAPI sur twitch.", token.getUserUuid()));
                }
                token.setTwitchUserId(twitchValidationTokenResponseDTO.getTwitchUserId());
                token.setTwitchUsername(twitchValidationTokenResponseDTO.getTwitchUsername());
                scopes = twitchValidationTokenResponseDTO.getScopes();
            } else {
                final Map<String, String> formRequest = new HashMap<>();
                formRequest.put("client_id", twitchApiConfig.getAppClientId());
                formRequest.put("client_secret", twitchApiConfig.getAppClientSecret());
                formRequest.put("grant_type", "refresh_token");
                formRequest.put("refresh_token", URLEncoder.encode(token.getRefreshToken(), StandardCharsets.UTF_8));
                final TwitchTokenResponseDTO tokenResponseDTO = this.twitchTokenAuthClient.getToken(formRequest);

                token.setAccessToken(tokenResponseDTO.getAccessToken());
                token.setRefreshToken(tokenResponseDTO.getRefreshToken());
                token.setExpirationDateToken(Date.from(Instant.now().plusSeconds(tokenResponseDTO.getExpiresIn() - 60L)));
                scopes = tokenResponseDTO.getScopes();
            }

            final TwitchClientTokenDTO tokenToSend = twitchClientTokenMapper.toDto(twitchClientTokenRepository.save(token));

            tokenToSend.setScopes(scopes);
            return tokenToSend;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new ApiForbiddenException(String.format("L'utilisateur %s à retiré l'accès à la FunixAPI sur twitch.", token.getUserUuid()));
            } else {
                throw new ApiException("Une erreur est survenue lors du refresh access token via Twitch.", e);
            }
        }
    }

    private void createEventSub(final String streamerName) {

    }

}
