package com.funixproductions.api.user.client.security;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserSession;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentSession {

    @Nullable
    public UserDTO getCurrentUser() {
        final UserSession userSession = getUserSession();
        if (userSession == null) {
            return null;
        }

        return userSession.getUserDTO();
    }

    @Nullable
    public String getCurrentUserIp() {
        final UserSession userSession = getUserSession();
        if (userSession == null) {
            return null;
        }

        return userSession.getIp();
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
