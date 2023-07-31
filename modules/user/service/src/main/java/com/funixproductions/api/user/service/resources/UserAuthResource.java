package com.funixproductions.api.user.service.resources;

import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetRequestDTO;
import com.funixproductions.api.user.service.services.CurrentSession;
import com.funixproductions.api.user.service.services.UserAuthService;
import com.funixproductions.api.user.service.services.UserResetService;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user/auth")
@RequiredArgsConstructor
public class UserAuthResource {

    private final UserAuthService userAuthService;
    private final UserResetService userResetService;
    private final CurrentSession currentSession;
    private final GoogleRecaptchaHandler captchaService;
    private static final String CAPTCHA_REGISTER = "register";
    private static final String CAPTCHA_LOGIN = "login";
    private static final String CAPTCHA_RESET_PASSWORD_REQUEST = "resetPasswordRequest";
    private static final String CAPTCHA_RESET_PASSWORD = "resetPassword";

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

    @PostMapping("resetPasswordRequest")
    void resetPasswordRequest(@RequestBody @Valid UserPasswordResetRequestDTO request, final HttpServletRequest servletRequest) {
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
    void resetPassword(@RequestBody @Valid UserPasswordResetDTO request, final HttpServletRequest servletRequest) {
        try {
            this.captchaService.verify(servletRequest, CAPTCHA_RESET_PASSWORD);
            this.userResetService.resetPassword(request);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de la réinitialisation du mot de passe.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }
}
