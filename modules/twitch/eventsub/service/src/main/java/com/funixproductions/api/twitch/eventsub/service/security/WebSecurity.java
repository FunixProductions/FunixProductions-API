package com.funixproductions.api.twitch.eventsub.service.security;

import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.client.security.ApiWebSecurity;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurity extends ApiWebSecurity {

    @NotNull
    @Override
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> getUrlsMatchers() {
        return ex -> ex
                .requestMatchers("/kubeinternal/**").permitAll()
                .requestMatchers("/ws/public/**").permitAll()
                .requestMatchers("/twitch/eventsub/cb**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().hasAuthority(UserRole.ADMIN.getRole());
    }
}
