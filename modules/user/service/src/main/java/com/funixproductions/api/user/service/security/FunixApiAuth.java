package com.funixproductions.api.user.service.security;

import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.services.UserCrudService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class FunixApiAuth implements AuthenticationManager {
    private final UserCrudService userCrudService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            final String username = authentication.getPrincipal().toString();
            final String password = authentication.getCredentials().toString();
            final User user = userCrudService.loadUserByUsername(username);

            if (user.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            } else {
                throw new ApiBadRequestException("Mauvais identifiants.");
            }
        } catch (Exception e) {
            throw new ApiBadRequestException("Mauvais identifiants.");
        }
    }
}
