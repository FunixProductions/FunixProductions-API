package com.funixproductions.api.user.service.components;

import com.funixproductions.api.encryption.client.clients.EncryptionClient;
import com.funixproductions.api.google.auth.client.clients.InternalGoogleAuthClient;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.recaptcha.client.clients.GoogleRecaptchaInternalClient;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public abstract class UserTestComponent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    protected EncryptionClient encryptionClient;

    @MockBean
    protected GoogleRecaptchaInternalClient googleCaptchaService;

    @MockBean
    protected InternalGoogleAuthClient googleAuthClient;

    @MockBean
    protected GoogleGmailClient googleGmailClient;

    public static final String PASSWORD = "ousddffdi22AA" + UUID.randomUUID();

    @BeforeEach
    public void setup() {
        when(encryptionClient.encrypt(anyString())).thenAnswer(invocation -> invocation.<String>getArgument(0));
        when(encryptionClient.decrypt(anyString())).thenAnswer(invocation -> invocation.<String>getArgument(0));

        doNothing().when(googleCaptchaService).verify(any(), any(), any());
        doNothing().when(googleAuthClient).deleteAllByUserUuidIn(anyList());
        doNothing().when(googleGmailClient).sendMail(any(), any());
    }

    public User createAdminAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.ADMIN);
        user.setCountryName("France");
        user.setCountryCode(250);
        user.setCountryCode2Chars("FR");
        user.setCountryCode3Chars("FRA");
        return userRepository.save(user);
    }

    public User createModoAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setRole(UserRole.MODERATOR);
        user.setCountryName("France");
        user.setCountryCode(250);
        user.setCountryCode2Chars("FR");
        user.setCountryCode3Chars("FRA");
        return userRepository.save(user);
    }

    public User createBasicUser() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setCountryName("France");
        user.setCountryCode(250);
        user.setCountryCode2Chars("FR");
        user.setCountryCode3Chars("FRA");
        return userRepository.save(user);
    }

    public UserTokenDTO loginUser(final User user) throws Exception {
        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(PASSWORD);
        userLoginDTO.setStayConnected(true);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        return jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

}
