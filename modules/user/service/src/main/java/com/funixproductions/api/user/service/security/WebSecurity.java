package com.funixproductions.api.user.service.security;

import com.funixproductions.api.encryption.client.utils.EncryptionString;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.client.security.ApiWebSecurity;
import com.funixproductions.api.user.service.services.UserCrudService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        http = http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                        .configurationSource(ApiWebSecurity.permitAllCors()))
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(Customizer.withDefaults())

                .authorizeHttpRequests(exchanges -> exchanges
                        .requestMatchers("/kubeinternal/user/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/user/auth/register**", "/user/auth/login**", "/user/auth/resetPassword**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/auth/current**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/auth/logout**").authenticated()
                        .requestMatchers("/user/auth/valid-account").authenticated()
                        .requestMatchers("/user/auth/sessions**").authenticated()
                        .requestMatchers("/user**").hasAuthority(UserRole.ADMIN.getRole())

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
    public PasswordEncoder passwordEncoder(EncryptionString encryptionString) {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return encryptionString.convertToDatabaseColumn(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                final String decodedPassword = encryptionString.convertToEntityAttribute(encodedPassword);
                return rawPassword.equals(decodedPassword);
            }
        };
    }

}
