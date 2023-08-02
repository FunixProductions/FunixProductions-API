package com.funixproductions.api.user.service.components;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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

    public static final String USER_PASSWORD = "clearpassword";

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final FunixProductionsEncryptionClient encryptionClient;
    private final JsonHelper jsonHelper;

    public User createAdminAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.ADMIN);

        Mockito.when(encryptionClient.encrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.decrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.encrypt(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(encryptionClient.decrypt(user.getPassword())).thenReturn(user.getPassword());
        return userRepository.save(user);
    }

    public User createModoAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.MODERATOR);

        Mockito.when(encryptionClient.encrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.decrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.encrypt(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(encryptionClient.decrypt(user.getPassword())).thenReturn(user.getPassword());
        return userRepository.save(user);
    }

    public User createBasicUser() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");

        Mockito.when(encryptionClient.encrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.decrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.encrypt(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(encryptionClient.decrypt(user.getPassword())).thenReturn(user.getPassword());
        return userRepository.save(user);
    }

    public UserTokenDTO loginUser(final User user) throws Exception {
        Mockito.when(encryptionClient.encrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.decrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(encryptionClient.encrypt(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(encryptionClient.decrypt(user.getPassword())).thenReturn(user.getPassword());

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
