package com.funixproductions.api.google.auth.service.resources;

import com.funixproductions.api.core.configs.FrontApplicationsDomainsConfig;
import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.api.google.auth.service.config.GoogleAuthConfig;
import com.funixproductions.api.google.auth.service.entities.GoogleAuthLinkUser;
import com.funixproductions.api.google.auth.service.repositories.GoogleAuthRepository;
import com.funixproductions.api.user.client.clients.InternalUserCrudClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/google/auth")
@RequiredArgsConstructor
public class GoogleAuthResource {

    private final GoogleIdTokenVerifier verifier;
    private final GoogleAuthRepository googleAuthRepository;
    private final InternalUserCrudClient userCrudClient;
    private final FrontApplicationsDomainsConfig frontApplicationsDomainsConfig;
    private final GoogleAuthConfig googleAuthConfig;

    @PostMapping("verifyGoogleIdToken")
    public ResponseEntity<String> verifyGoogleIdToken(@RequestParam String credential, @RequestParam(required = false) String origin) {

        try {
            final GoogleIdToken token = this.verifier.verify(credential);

            if (token != null) {
                this.googleAuthConfig.checkAudience(token);

                final GoogleIdToken.Payload payload = token.getPayload();
                final UserDTO userDTO = updateUserProfileFromGoogleToken(payload);

                final String successString = String.format("User auth success with google, userDTO id: %s, name: %s, email: %s", userDTO.getId(), userDTO.getUsername(), userDTO.getEmail());
                log.info(successString);

                final FrontOrigins redirectOrigin = FrontOrigins.getRedirectAuthOrigin(origin);
                if (redirectOrigin == null) {
                    return new ResponseEntity<>(successString, HttpStatus.OK);
                } else {
                    return generateRedirectOrigin(redirectOrigin, userDTO);
                }
            } else {
                throw new ApiForbiddenException("Invalid Id Token");
            }
        } catch (GeneralSecurityException | IOException generalSecurityException) {
            throw new ApiException("Error while verifying google id token", generalSecurityException);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Fatal error while verifying google id token", e);
            throw new ApiException("Fatal error while verifying google id token", e);
        }
    }

    private UserDTO updateUserProfileFromGoogleToken(GoogleIdToken.Payload payload) throws ApiException {
        final String googleUserId = payload.getSubject();
        final String email = payload.getEmail();
        final String name = (String) payload.get("name");

        final GoogleAuthLinkUser googleAuthLinkUser = findAuthByGoogleId(googleUserId);

        if (googleAuthLinkUser == null) {
            return createNewUserWithGoogleAuth(name, email, googleUserId);
        } else {
            final UserDTO userDTO = this.userCrudClient.findById(googleAuthLinkUser.getUserUuid());

            final UserSecretsDTO userSecretsDTO = new UserSecretsDTO();
            userSecretsDTO.setId(userDTO.getId());
            userSecretsDTO.setUsername(name);
            userSecretsDTO.setEmail(email);

            return this.userCrudClient.update(userSecretsDTO);
        }
    }

    @Nullable
    private GoogleAuthLinkUser findAuthByGoogleId(final String googleUserId) {
        final Optional<GoogleAuthLinkUser> search = this.googleAuthRepository.findByGoogleUserId(googleUserId);
        return search.orElse(null);
    }

    private UserDTO createNewUserWithGoogleAuth(final String name, final String email, final String googleUserId) {
        final String password = generateGoogleAuthPassword();

        final UserSecretsDTO userCreationDTO = new UserSecretsDTO();
        userCreationDTO.setUsername(name);
        userCreationDTO.setEmail(email);
        userCreationDTO.setPassword(password);
        userCreationDTO.setRole(UserRole.USER);
        final UserDTO userCreated = this.userCrudClient.create(userCreationDTO);

        this.googleAuthRepository.save(new GoogleAuthLinkUser(userCreated.getId().toString(), googleUserId));
        return userCreated;
    }

    private String generateGoogleAuthPassword() {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        final SecureRandom secureRandom = new SecureRandom();

        passwordGenerator.setAlphaDown(secureRandom.nextInt(15) + 3);
        passwordGenerator.setAlphaUpper(secureRandom.nextInt(15) + 3);
        passwordGenerator.setNumbersAmount(secureRandom.nextInt(15) + 3);
        passwordGenerator.setSpecialCharsAmount(secureRandom.nextInt(15) + 3);
        return passwordGenerator.generateRandomPassword();
    }

    private ResponseEntity<String> generateRedirectOrigin(@NonNull final FrontOrigins frontOrigins,
                                                          @NonNull final UserDTO userDTO) {
        final String domain = switch (frontOrigins) {
            case FUNIX_PRODUCTIONS_DASHBOARD -> this.frontApplicationsDomainsConfig.getDashboardDomain();
            case PACIFISTA_PUBLIC_WEB -> this.frontApplicationsDomainsConfig.getPacifistaFrontDomain();
        };
        final String jwtToken = generateJwtToken(userDTO);
        final String path = domain + "/captureAuth?jwt=" + jwtToken;

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(path))
                .build();
    }

    private String generateJwtToken(@NonNull final UserDTO userDTO) {
        try {
            final UserTokenDTO jwtToken = this.userCrudClient.generateAccessToken(userDTO);
            return jwtToken.getToken();
        } catch (FeignException e) {
            log.warn("Erreur lors de la génération du token JWT.", e);
            throw new ApiBadRequestException("Erreur lors de la génération du token JWT.", e);
        }
    }

}
