package com.funixproductions.api.user.service.components;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class UserTestComponent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JsonHelper jsonHelper;

    @MockBean
    private FunixProductionsEncryptionClient encryptionClient;

    public User createAdminAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.ADMIN);

        when(encryptionClient.encrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        when(encryptionClient.decrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        return userRepository.save(user);
    }

    public User createModoAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.MODERATOR);

        when(encryptionClient.encrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        when(encryptionClient.decrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        return userRepository.save(user);
    }

    public User createBasicUser() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID() + "ousddffdi22AA");
        user.setEmail(UUID.randomUUID() + "@gmail.com");

        when(encryptionClient.encrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        when(encryptionClient.decrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        return userRepository.save(user);
    }

    public UserTokenDTO loginUser(final User user) throws Exception {
        when(encryptionClient.encrypt(eq(user.getPassword()))).thenReturn(user.getPassword());
        when(encryptionClient.decrypt(eq(user.getPassword()))).thenReturn(user.getPassword());

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
