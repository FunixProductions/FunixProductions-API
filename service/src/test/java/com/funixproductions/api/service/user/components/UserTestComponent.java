package com.funixproductions.api.service.user.components;

import com.funixproductions.api.client.user.dtos.UserTokenDTO;
import com.funixproductions.api.client.user.dtos.requests.UserLoginDTO;
import com.funixproductions.api.client.user.enums.UserRole;
import com.funixproductions.api.service.user.entities.User;
import com.funixproductions.api.service.user.repositories.UserRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserTestComponent {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final JsonHelper jsonHelper;

    public User createAdminAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.ADMIN);

        return userRepository.save(user);
    }

    public User createModoAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.MODERATOR);

        return userRepository.save(user);
    }

    public User createBasicUser() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");

        return userRepository.save(user);
    }

    public UserTokenDTO loginUser(final User user) throws Exception {
        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());
        userLoginDTO.setStayConnected(true);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        return jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

}
