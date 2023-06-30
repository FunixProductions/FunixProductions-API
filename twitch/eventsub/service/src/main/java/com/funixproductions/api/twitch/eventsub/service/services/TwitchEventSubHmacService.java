package com.funixproductions.api.twitch.eventsub.service.services;

import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service used to handle the encryption with the hmac shared twitch key
 */
@Service
public class TwitchEventSubHmacService {

    public static final String FILE_SECRET_NAME = "twitch-eventsub-secret.key";

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HMAC_PREFIX = "sha256=";

    public static final String TWITCH_MESSAGE_ID = "Twitch-Eventsub-Message-Id";
    public static final String TWITCH_MESSAGE_TIMESTAMP = "Twitch-Eventsub-Message-Timestamp";
    public static final String TWITCH_MESSAGE_SIGNATURE = "Twitch-Eventsub-Message-Signature";

    private final String key;
    private final Mac mac;

    public TwitchEventSubHmacService() {
        this.key = getAppSecret();
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);

        try {
            this.mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new ApiException(String.format("L'lagorithme HMAC %s n'existe pas.", HMAC_ALGORITHM), noSuchAlgorithmException);
        } catch (InvalidKeyException invalidKeyException) {
            throw new ApiException("La clé d'encryption pour HMAC twitch est invalide.", invalidKeyException);
        }
    }

    /**
     * Uses this to verify that the callback is called by Twitch.
     * @param request the request to get the headers
     * @param body request body
     * @throws ApiBadRequestException when the header signature is not from twitch
     */
    public void validEventMessage(final HttpServletRequest request, final byte[] body) throws ApiBadRequestException {
        final String messageTimestamp = request.getHeader(TWITCH_MESSAGE_TIMESTAMP);
        if (messageTimestamp == null) {
            throw new ApiBadRequestException("Il manque le message timestamp.");
        }

        final String messageSignature = request.getHeader(TWITCH_MESSAGE_SIGNATURE);
        if (messageSignature == null || !messageSignature.startsWith(HMAC_PREFIX)) {
            throw new ApiBadRequestException("Il manque la signature twitch");
        }

        final Instant messageTs = Instant.parse(messageTimestamp);
        if (messageTs.plus(10, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            throw new ApiBadRequestException("La notification twitch est trop vielle.");
        }

        final byte[] twitchHmac = bytesToHex(getHmacFromTwitch(request, body));
        final byte[] expectedHmac = messageSignature.substring(HMAC_PREFIX.length()).getBytes(StandardCharsets.UTF_8);

        if (!MessageDigest.isEqual(twitchHmac, expectedHmac)) {
            throw new ApiBadRequestException("La clé fournie en header ne correspond pas avec la clé de la funix api.");
        }
    }

    /**
     * Method to get the key used to encode messages, twitch need this
     * @return String key
     */
    public String getKey() {
        return key;
    }

    private byte[] getHmacFromTwitch(final HttpServletRequest request, final byte[] body) {
        final byte[] messageId = request.getHeader(TWITCH_MESSAGE_ID).getBytes(StandardCharsets.UTF_8);
        final byte[] messageTs = request.getHeader(TWITCH_MESSAGE_TIMESTAMP).getBytes(StandardCharsets.UTF_8);
        final byte[] message = new byte[messageId.length + messageTs.length + body.length];

        System.arraycopy(messageId, 0, message, 0, messageId.length);
        System.arraycopy(messageTs, 0, message, messageId.length, messageTs.length);
        System.arraycopy(body, 0, message, messageId.length + messageTs.length, body.length);

        return mac.doFinal(message);
    }

    private static String getAppSecret() {
        final File secretFile = new File(FILE_SECRET_NAME);
        String secret = readSecretFromFile(secretFile);

        if (secret != null) {
            return secret;
        } else {
            secret = generateNewSecret();

            try {
                if (!secretFile.exists() && !secretFile.createNewFile()) {
                    throw new ApiException("Impossible de créer le fichier qui contient la clé d'encryption twitch hmac.");
                }

                Files.writeString(
                        secretFile.toPath(),
                        secret,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
                return secret;
            } catch (IOException e) {
                throw new ApiException("Erreur lors de la création d'un nouveau fichier d'encryption twitch eventsub.", e);
            }
        }
    }

    @Nullable
    private static String readSecretFromFile(final File file) {
        if (!file.exists()) {
            return null;
        } else {
            try {
                return Files.readString(file.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ApiException("Erreur lors de la récupération du token secret pour les event sub twitch.", e);
            }
        }
    }

    private static String generateNewSecret() {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setAlphaUpper(15);
        passwordGenerator.setAlphaDown(15);
        passwordGenerator.setNumbersAmount(10);
        passwordGenerator.setSpecialCharsAmount(0);

        return passwordGenerator.generateRandomPassword();
    }

    public static byte[] bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }
}
