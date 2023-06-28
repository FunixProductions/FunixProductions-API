package com.funixproductions.api.service.google.auth.resources;

import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.client.user.dtos.UserTokenDTO;
import com.funixproductions.api.client.user.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.client.user.enums.UserRole;
import com.funixproductions.api.service.core.configs.FunixProductionsAppConfiguration;
import com.funixproductions.api.service.core.enums.RedirectAuthOrigin;
import com.funixproductions.api.service.google.auth.config.GoogleAuthConfig;
import com.funixproductions.api.service.google.auth.entities.GoogleAuthLinkUser;
import com.funixproductions.api.service.google.auth.repositories.GoogleAuthRepository;
import com.funixproductions.api.service.google.core.configs.GoogleCoreConfig;
import com.funixproductions.api.service.user.services.UserCrudService;
import com.funixproductions.api.service.user.services.UserTokenService;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
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
    private final UserCrudService userCrudService;
    private final FunixProductionsAppConfiguration appConfiguration;
    private final UserTokenService tokenService;
    private final GoogleAuthConfig googleTokensConfig;
    private final GoogleCoreConfig googleCoreConfig;

    @PostMapping("verifyGoogleIdToken")
    public ResponseEntity<String> verifyGoogleIdToken(@RequestParam String credential, @RequestParam(required = false) String origin) {

        try {
            final GoogleIdToken token = this.verifier.verify(credential);

            if (token != null) {
                this.googleCoreConfig.checkAudience(token, "");

                final GoogleIdToken.Payload payload = token.getPayload();
                final UserDTO userDTO = updateUserProfileFromGoogleToken(payload);

                final String successString = String.format("User auth success with google, userDTO id: %s, name: %s, email: %s", userDTO.getId(), userDTO.getUsername(), userDTO.getEmail());
                log.info(successString);

                final RedirectAuthOrigin redirectOrigin = RedirectAuthOrigin.getRedirectAuthOrigin(origin);
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
            final UserDTO userDTO = this.userCrudService.findById(googleAuthLinkUser.getUserUuid());
            userDTO.setUsername(name);
            userDTO.setEmail(email);

            return this.userCrudService.update(userDTO);
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
        final UserDTO userCreated = this.userCrudService.create(userCreationDTO);

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

    private ResponseEntity<String> generateRedirectOrigin(@NonNull final RedirectAuthOrigin redirectAuthOrigin,
                                                          @NonNull final UserDTO userDTO) {
        final String domain = switch (redirectAuthOrigin) {
            case FUNIX_PRODUCTIONS_DASHBOARD -> this.appConfiguration.getFunixproductionsWebDashboardDomain();
            case PACIFISTA_PUBLIC_WEB -> this.appConfiguration.getPacifistaWebPublicWebsiteDomain();
        };
        final String jwtToken = generateJwtToken(userDTO);
        final String path = domain + "/captureAuth?jwt=" + jwtToken;

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(path))
                .build();
    }

    private String generateJwtToken(@NonNull final UserDTO userDTO) {
        final UserTokenDTO jwtToken = this.tokenService.generateAccessToken(userDTO);
        return jwtToken.getToken();
    }

}
