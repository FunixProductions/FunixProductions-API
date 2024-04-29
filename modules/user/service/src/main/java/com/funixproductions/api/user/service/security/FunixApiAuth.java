package com.funixproductions.api.user.service.security;

import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.services.UserCrudService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class FunixApiAuth implements AuthenticationManager {

    private final UserCrudService userCrudService;
    private final PasswordEncoder passwordEncoder;

    private static final String BAD_CREDENTIALS = "Mot de passe ou nom d'utilisateur incorrect.";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            final User user = userCrudService.loadUserByUsername(authentication.getPrincipal().toString());

            if (this.passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            } else {
                throw new ApiBadRequestException(BAD_CREDENTIALS);
            }
        } catch (Exception e) {
            throw new ApiBadRequestException(BAD_CREDENTIALS, e);
        }
    }
}
