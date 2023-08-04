package com.funixproductions.api.user.service.resources;

import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetRequestDTO;
import com.funixproductions.api.user.service.services.*;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.crud.enums.SearchOperation;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("user/auth")
@RequiredArgsConstructor
public class UserAuthResource {

    private static final String CAPTCHA_REGISTER = "register";
    private static final String CAPTCHA_LOGIN = "login";
    private static final String CAPTCHA_RESET_PASSWORD_REQUEST = "resetPasswordRequest";
    private static final String CAPTCHA_RESET_PASSWORD = "resetPassword";

    private final UserAuthService userAuthService;
    private final UserTokenService userTokenService;
    private final UserResetService userResetService;
    private final UserValidationAccountService userValidationAccountService;

    private final CurrentSession currentSession;
    private final GoogleRecaptchaHandler captchaService;

    @PostMapping("register")
    public UserDTO register(@RequestBody @Valid UserCreationDTO request, final HttpServletRequest servletRequest) {
        try {
            captchaService.verify(servletRequest, CAPTCHA_REGISTER);
            return userAuthService.register(request);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de l'inscription de l'utilisateur.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @PostMapping("login")
    public UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, final HttpServletRequest servletRequest) {
        try {
            captchaService.verify(servletRequest, CAPTCHA_LOGIN);
            return userAuthService.login(request, servletRequest);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la connexion de l'utilisateur.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @PostMapping("logout")
    public void logout() {
        try {
            userAuthService.logoutSession();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la déconnexion de l'utilisateur.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @GetMapping("current")
    public UserDTO currentUser() {
        try {
            final UserDTO userDTO = currentSession.getCurrentUser();

            if (userDTO == null) {
                throw new ApiForbiddenException("Vous n'êtes pas connecté.");
            } else {
                return userDTO;
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la récupération de l'utilisateur courant.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @GetMapping("sessions")
    public PageDTO<UserTokenDTO> getActualSessions(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String elementsPerPage) {
        try {
            final UserDTO currentUser = this.currentSession.getCurrentUser();

            if (currentUser == null) {
                throw new ApiForbiddenException("Vous n'êtes pas connecté.");
            } else {
                return userTokenService.getAll(page, elementsPerPage, String.format("user.uuid:%s:%s", SearchOperation.EQUALS.getOperation(), currentUser.getId()), "createdAt:desc");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la récupération des sessions actuelles.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @DeleteMapping("sessions")
    public void removeSession(@RequestParam String[] id) {
        try {
            final UserDTO currentUser = this.currentSession.getCurrentUser();

            if (currentUser == null) {
                throw new ApiForbiddenException("Vous n'êtes pas connecté.");
            } else {
                final PageDTO<UserTokenDTO> tokens = userTokenService.getAll(
                        "0",
                        Integer.toString(id.length),
                        String.format("uuid:%s:[%s]", SearchOperation.EQUALS.getOperation(), String.join("|", id)),
                        "createdAt:desc"
                );
                final Set<String> tokensToRemove = new HashSet<>();

                for (final UserTokenDTO token : tokens.getContent()) {
                    if (token.getUser().getId().equals(currentUser.getId())) {
                        tokensToRemove.add(token.getId().toString());
                    }
                }
                this.userTokenService.delete(tokensToRemove.toArray(new String[0]));
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la suppressions des sessions actuelles.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @PostMapping("resetPasswordRequest")
    public void resetPasswordRequest(@RequestBody @Valid UserPasswordResetRequestDTO request, final HttpServletRequest servletRequest) {
        try {
            this.captchaService.verify(servletRequest, CAPTCHA_RESET_PASSWORD_REQUEST);
            this.userResetService.resetPasswordRequest(request, servletRequest);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la demande de réinitialisation du mot de passe.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @PostMapping("resetPassword")
    public void resetPassword(@RequestBody @Valid UserPasswordResetDTO request, final HttpServletRequest servletRequest) {
        try {
            this.captchaService.verify(servletRequest, CAPTCHA_RESET_PASSWORD);
            this.userResetService.resetPassword(request, servletRequest);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la réinitialisation du mot de passe.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @GetMapping("valid-account")
    public ResponseEntity<String> validAccount(@RequestParam String token) {
        try {
            final UserDTO currentUser = this.currentSession.getCurrentUser();

            if (currentUser == null) {
                throw new ApiForbiddenException("Vous n'êtes pas connecté.");
            } else {
                this.userValidationAccountService.validateAccount(currentUser, token);
                return ResponseEntity.ok("Votre compte a été validé avec succès. Vous pouvez fermer cette page.");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la validation du compte.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }

    @PostMapping("valid-account")
    public void requestNewValidationCodeAccount() {
        try {
            final UserDTO currentUser = this.currentSession.getCurrentUser();

            if (currentUser == null) {
                throw new ApiForbiddenException("Vous n'êtes pas connecté.");
            } else {
                this.userValidationAccountService.requestNewToken(currentUser);
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la demande d'un nouveau code de validation.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }
}
