package com.funixproductions.api.twitch.auth.service.security;

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
        return exchanges -> exchanges
                .requestMatchers("/kubeinternal/twitch/auth/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                .requestMatchers("/twitch/auth/cb**").permitAll()
                .anyRequest().authenticated();
    }
}
