package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.user.service.services.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JwtTokenGeneratorTest {

    @Test
    void generateToken() {
        final String jwtSecret = UserTokenService.generateJwtSecretKey();

        log.info("jwtSecret: {}", jwtSecret);
        assertTrue(Strings.isNotBlank(jwtSecret));
        assertNotEquals(jwtSecret, UserTokenService.generateJwtSecretKey());
    }

}
