package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.user.client.dtos.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class TestUserDTOGenerationTest {

    @Test
    void testFakeData() {
        final UserDTO userDTO = UserDTO.generateFakeDataForTestingPurposes();

        assertNotNull(userDTO);
        assertNotNull(userDTO.getRole());
        assertNotNull(userDTO.getUsername());
        assertNotNull(userDTO.getValid());
        assertNotNull(userDTO.getCountry());
        assertNotNull(userDTO.getCreatedAt());
        assertNotNull(userDTO.getUpdatedAt());
        assertNotNull(userDTO.getId());
    }

}
