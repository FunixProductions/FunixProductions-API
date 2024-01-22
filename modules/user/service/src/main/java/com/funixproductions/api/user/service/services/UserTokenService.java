package com.funixproductions.api.user.service.services;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.service.configs.UserConfiguration;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserSession;
import com.funixproductions.api.user.service.entities.UserToken;
import com.funixproductions.api.user.service.mappers.UserTokenMapper;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.api.user.service.repositories.UserTokenRepository;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import com.funixproductions.core.exceptions.ApiUnauthorizedException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.funixproductions.core.tools.time.TimeUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserTokenService extends ApiService<UserTokenDTO, UserToken, UserTokenMapper, UserTokenRepository> {
    private static final String ISSUER = "FunixProductionsApi - api.funixproductons.com";

    private final UserTokenRepository tokenRepository;
    private final UserTokenMapper tokenMapper;
    private final UserRepository userRepository;

    private final SecretKey jwtSecretKey;

    public UserTokenService(UserTokenRepository tokenRepository,
                            UserTokenMapper tokenMapper,
                            UserRepository userRepository,
                            UserConfiguration userConfiguration) {
        super(tokenRepository, tokenMapper);
        this.tokenRepository = tokenRepository;
        this.tokenMapper = tokenMapper;
        this.userRepository = userRepository;
        this.jwtSecretKey = getJwtCryptKey(userConfiguration);
    }

    /**
     * Method used to generate access token for user (oauth)
     * @param userDTO userdto
     * @return token
     */
    @Transactional
    public UserTokenDTO generateAccessToken(final UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new ApiBadRequestException("L'utilisateur demandé ne possède pas d'id.");
        }

        final User user = userRepository.findByUuid(userDTO.getId().toString())
                .orElseThrow(() -> new ApiNotFoundException(String.format("User with id %s not found", userDTO.getId())));
        return generateAccessToken(user, false);
    }

    /**
     * Generate one week token usage expiration or lifetime token
     * @param user user to generate token
     * @return access token valid
     */
    @Transactional
    public UserTokenDTO generateAccessToken(final User user, boolean stayConnected) {
        final Date expirationDate = stayConnected ? null : Date.from(Instant.now().plusSeconds(604800));
        final UserToken userToken = new UserToken();

        userToken.setUser(user);
        userToken.setUuid(UUID.randomUUID());
        userToken.setExpirationDate(expirationDate);
        userToken.setToken(Jwts.builder()
                .subject(userToken.getUuid().toString())
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(jwtSecretKey)
                .audience().add(user.getRole().getRole()).and()
                .compact());

        return tokenMapper.toDto(tokenRepository.save(userToken));
    }

    /**
     * Invalidate token by session
     * @param userSession user session created on bearer fetch
     */
    @Transactional
    public void invalidToken(@NonNull final UserSession userSession) {
        final Optional<UserToken> search = tokenRepository.findByToken(userSession.getBearerToken());
        search.ifPresent(this.tokenRepository::delete);
    }

    /**
     * Check if a jwt token is valid
     * @param token jwt token
     * @return user token valid, null if not
     */
    @Nullable
    public User isTokenValid(final String token) {
        try {
            Jwts.parser()
                    .decryptWith(jwtSecretKey)
                            .build()
                                    .parse(token);

            final UserToken userToken = getToken(token);
            if (userToken != null) {
                return userToken.getUser();
            } else {
                return null;
            }
        } catch (ExpiredJwtException expiredJwtException) {
            throw new ApiUnauthorizedException("Le token JWT utilisateur est expiré.", expiredJwtException);
        } catch (MalformedJwtException malformedJwtException) {
            throw new ApiUnauthorizedException("Le token JWT utilisateur est malformé.", malformedJwtException);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new ApiUnauthorizedException("Le token JWT utilisateur n'est pas supporté.", unsupportedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ApiUnauthorizedException("Le token JWT utilisateur est invalide.", illegalArgumentException);
        } catch (io.jsonwebtoken.security.SignatureException signatureException) {
            throw new ApiUnauthorizedException("Le token JWT utilisateur n'est pas signé correctement.", signatureException);
        } catch (SecurityException securityException) {
            throw new ApiUnauthorizedException("Le token JWT utilisateur n'est pas sécurisé.", securityException);
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la validation du token.", e);
        }
    }

    @Nullable
    private UserToken getToken(final String token) {
        final Claims claims = Jwts.parser()
                .decryptWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        final String tokenUuid = claims.getSubject();
        final Optional<UserToken> search = tokenRepository.findByUuid(tokenUuid);

        return search.orElse(null);
    }

    private static SecretKey getJwtCryptKey(final UserConfiguration userConfiguration) {
        if (Strings.isEmpty(userConfiguration.getJwtSecret())) {
            throw new ApiException("JWT secret key is not defined in UserConfiguration.");
        }

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(userConfiguration.getJwtSecret()));
    }

    /**
     * Generate a new secret key for jwt for this application.
     * @return base64 encoded jwt secret key
     */
    public static String generateJwtSecretKey() {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setAlphaDown(150);
        passwordGenerator.setAlphaUpper(150);
        passwordGenerator.setNumbersAmount(150);
        passwordGenerator.setSpecialCharsAmount(150);

        final Key jwtSecretKey = Keys.hmacShaKeyFor(passwordGenerator.generateRandomPassword().getBytes(StandardCharsets.UTF_8));
        return Encoders.BASE64.encode(jwtSecretKey.getEncoded());
    }

    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.MINUTES)
    public void removeInvalidTokens() {
        final Iterable<UserToken> tokens = this.tokenRepository.findAll();
        final Instant start = Instant.now();
        int invalidedTokens = 0;

        for (final UserToken token : tokens) {
            final Instant expirationDate = token.getExpirationDate();

            if (expirationDate != null && expirationDate.isBefore(start)) {
                invalidedTokens++;
                this.tokenRepository.delete(token);
            }
        }

        if (invalidedTokens > 0) {
            final long seconds = TimeUtils.diffInMillisBetweenInstants(start, Instant.now());
            log.info("{} tokens user invalides supprimés. ({} ms)", invalidedTokens, seconds);
        }
    }
}
