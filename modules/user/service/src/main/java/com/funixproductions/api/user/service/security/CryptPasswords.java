package com.funixproductions.api.user.service.security;

import com.funixproductions.api.user.service.services.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CryptPasswords {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                64,
                128,
                1,
                60000,
                10
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(UserCrudService userCrudService,
                                                       PasswordEncoder passwordEncoder) {
        return new FunixApiAuth(userCrudService, passwordEncoder);
    }

}
