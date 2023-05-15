package com.funixproductions.api.service.user.services;

import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.service.user.entities.UserSession;
import com.funixproductions.api.service.user.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentSession {

    private final UserMapper userMapper;

    @Nullable
    public UserDTO getCurrentUser() {
        final UserSession userSession = getUserSession();
        if (userSession == null) {
            return null;
        }

        return userMapper.toDto(userSession.getUser());
    }

    @Nullable
    public UserSession getUserSession() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return null;
        }

        final Object principal = authentication.getPrincipal();
        if (principal instanceof final UserSession userSession) {
            return userSession;
        } else {
            return null;
        }
    }

}
