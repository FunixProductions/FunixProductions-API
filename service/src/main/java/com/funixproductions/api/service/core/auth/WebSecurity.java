package com.funixproductions.api.service.core.auth;

import com.funixproductions.api.client.user.enums.UserRole;
import com.funixproductions.api.service.user.components.FunixApiAuth;
import com.funixproductions.api.service.user.services.UserCrudService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final UserCrudService userCrudService;
    private final JwtTokenFilter jwtTokenFilter;

    public WebSecurity(UserCrudService userCrudService,
                       JwtTokenFilter jwtTokenFilter) {
        this.userCrudService = userCrudService;
        this.jwtTokenFilter = jwtTokenFilter;

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http.cors(httpSecurityCorsConfigurer ->httpSecurityCorsConfigurer.configurationSource(httpServletRequest -> {
                    final CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.addAllowedOriginPattern("*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedMethod("*");
                    return corsConfiguration;
                })).csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(Customizer.withDefaults())

                .authorizeHttpRequests(exchanges -> exchanges
                        .requestMatchers("/mail**").hasAuthority(UserRole.ADMIN.getRole())

                        .requestMatchers(HttpMethod.POST, "/user/auth/register**", "/user/auth/login**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/auth/current**").authenticated()
                        .requestMatchers("/user**").hasAuthority(UserRole.ADMIN.getRole())

                        .requestMatchers("/twitch/auth/cb").permitAll()
                        .requestMatchers("/twitch/eventsub/cb").permitAll()
                        .requestMatchers("/twitch/eventsub**").hasAuthority(UserRole.ADMIN.getRole())
                        .requestMatchers(HttpMethod.GET, "/twitch/*/funix").permitAll()

                        .requestMatchers("/google/recaptcha/verify**").permitAll()

                        .requestMatchers("/ws**").permitAll()

                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new FunixApiAuth(userCrudService);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
