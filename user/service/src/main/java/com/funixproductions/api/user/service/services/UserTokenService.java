package com.funixproductions.api.user.service.services;

import com.funixproductions.api.user.dtos.UserDTO;
import com.funixproductions.api.user.dtos.UserTokenDTO;
import com.funixproductions.api.user.service.entities.UserSession;
import com.funixproductions.api.user.service.entities.UserToken;
import com.funixproductions.api.user.service.mappers.UserTokenMapper;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.api.user.service.repositories.UserTokenRepository;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import com.funixproductions.core.tools.time.TimeUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserTokenService {
    private static final String ISSUER = "FunixProductionsApi - api.funixproductons.com";

    private final UserTokenRepository tokenRepository;
    private final UserTokenMapper tokenMapper;
    private final UserRepository userRepository;

    private final Key jwtSecretKey;

    public UserTokenService(UserTokenRepository tokenRepository,
                            UserTokenMapper tokenMapper,
                            UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.tokenMapper = tokenMapper;
        this.userRepository = userRepository;
        this.jwtSecretKey = getJwtCryptKey();
    }

    /**
     * Method used to generate access token for user (oauth)
     * @param userDTO userdto
     * @return token
     */
    public UserTokenDTO generateAccessToken(final UserDTO userDTO) {
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
                .setSubject(userToken.getUuid().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(jwtSecretKey)
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
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

            final UserToken userToken = getToken(token);

            if (userToken != null) {
                return userToken.getUser();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private UserToken getToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        final String tokenUuid = claims.getSubject();
        final Optional<UserToken> search = tokenRepository.findByUuid(tokenUuid);

        return search.orElse(null);
    }

    private static Key getJwtCryptKey() {
        final File keyFile = new File("secretJwt.key");
        Key key = getKeyFromFile(keyFile);

        if (key == null) {
            key = generateNewKey();
            saveKey(key, keyFile);
            return key;
        } else {
            return key;
        }
    }

    @Nullable
    private static Key getKeyFromFile(final File keyFile) throws ApiException {
        try {
            if (keyFile.exists()) {
                final String content = Files.readString(keyFile.toPath(), StandardCharsets.UTF_8);

                if (!Strings.isEmpty(content)) {
                    final byte[] decodedKey = Decoders.BASE64.decode(content);
                    return Keys.hmacShaKeyFor(decodedKey);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new ApiException("Une erreur est survenue lors du chargement de la clé secrete pour les tokens JWT.", e);
        }
    }

    private static Key generateNewKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    private static void saveKey(final Key key, final File keyFile) {
        try {
            if (!keyFile.exists() && !keyFile.createNewFile()) {
                throw new IOException("Creation file failed.");
            }

            final String encodedKey = Encoders.BASE64.encode(key.getEncoded());
            Files.writeString(keyFile.toPath(), encodedKey, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ApiException("Impossible de sauvegarder la clé de cryptage pour les tokens JWT.");
        }
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
