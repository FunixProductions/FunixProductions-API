package com.funixproductions.api.user.service.security;

import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserSession;
import com.funixproductions.api.user.service.services.UserTokenService;
import com.funixproductions.core.tools.network.IPUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserTokenService tokenService;
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

        final String token = bearerTokenHeader.split(" ")[1].trim();

        try {
            final User user = tokenService.isTokenValid(token);
            if (user == null) {
                chain.doFilter(request, response);
                return;
            }

            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    new UserSession(
                            user,
                            token,
                            ipUtils.getClientIp(request)
                    ),
                    null,
                    user.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception e) {
            chain.doFilter(request, response);
        }
    }

}
