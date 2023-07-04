package com.funixproductions.api.user.client.security;

import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserSession;
import com.funixproductions.core.tools.network.IPUtils;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserJwtTokenFilter extends OncePerRequestFilter {

    private final UserAuthClient userAuthClient;
    private final IPUtils ipUtils;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain chain) throws ServletException, IOException {
        final String bearerTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isEmpty(bearerTokenHeader) || !bearerTokenHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            final UserDTO user = userAuthClient.current(bearerTokenHeader);
            final UserSession userSession = new UserSession(user, ipUtils.getClientIp(request));
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userSession,
                    null,
                    getAuthorities(user)
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (FeignException e) {
            chain.doFilter(request, response);
        }
    }

    private List<SimpleGrantedAuthority> getAuthorities(final UserDTO user) {
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (final String authority : user.getRole().getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }
}
