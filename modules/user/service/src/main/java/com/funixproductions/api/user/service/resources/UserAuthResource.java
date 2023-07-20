package com.funixproductions.api.user.service.resources;

import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.service.services.CurrentSession;
import com.funixproductions.api.user.service.services.UserAuthService;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.tools.network.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
@RequiredArgsConstructor
public class UserAuthResource {

    private final UserAuthService userAuthService;
    private final CurrentSession currentSession;
    private final GoogleRecaptchaHandler captchaService;
    private final IPUtils ipUtils;
    private static final String CAPTCHA_REGISTER = "register";
    private static final String CAPTCHA_LOGIN = "login";

    @PostMapping("register")
    public UserDTO register(@RequestBody @Valid UserCreationDTO request, final HttpServletRequest servletRequest) {
        captchaService.verify(servletRequest, CAPTCHA_REGISTER);

        return userAuthService.register(request);
    }

    @PostMapping("login")
    public UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, final HttpServletRequest servletRequest) {
        captchaService.verify(servletRequest, CAPTCHA_LOGIN);

        return userAuthService.login(request, servletRequest);
    }

    @PostMapping("logout")
    public void logout() {
        userAuthService.logoutSession();
    }

    @GetMapping("current")
    public UserDTO currentUser() {
        final UserDTO userDTO = currentSession.getCurrentUser();

        if (userDTO == null) {
            throw new ApiForbiddenException("Vous n'êtes pas connecté.");
        } else {
            return userDTO;
        }
    }

    @GetMapping("ip")
    public String getIp(final HttpServletRequest servletRequest) {
        return this.ipUtils.getClientIp(servletRequest);
    }
}
