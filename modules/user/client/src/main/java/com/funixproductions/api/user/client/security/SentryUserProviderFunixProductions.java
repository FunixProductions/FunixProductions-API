package com.funixproductions.api.user.client.security;

import com.funixproductions.api.user.client.dtos.UserDTO;
import io.sentry.protocol.User;
import io.sentry.spring.jakarta.SentryUserProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SentryUserProviderFunixProductions implements SentryUserProvider {

    private final CurrentSession currentSession;

    @Override
    public @Nullable User provideUser() {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null) {
            return null;
        }

        final User user = new User();
        user.setId(userDTO.getId().toString());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setIpAddress(currentSession.getCurrentUserIp());

        return user;
    }
}
