package com.weatherforecast.controller;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.entity.User.Role;
import com.weatherforecast.entity.User.Status;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
public class PublicControllerConfirmationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private org.springframework.boot.CommandLineRunner commandLineRunner;

    @AfterEach
    void tearDown() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testConfirmationWithValidCode() throws Exception {
        User user = User.builder()
                .name("testUser")
                .email("test@company.com")
                .hashPassword(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .status(Status.NOT_CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);

        String validCode = "123456";
        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code(validCode)
                .user(savedUser)
                .expireDataTime(LocalDateTime.now().plusHours(24))
                .isConfirmed(false)
                .build();
        confirmationCodeRepository.save(confirmationCode);

        String requestPath = "/api/public/confirmation";

        mockMvc.perform(get(requestPath)
                        .param("code", validCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Email test@company.com is successfully confirmed"));
    }

    @Test
    void testConfirmationWithInvalidCode() throws Exception {
        String invalidCode = "invalidCode";

        String requestPath = "/api/public/confirmation";

        mockMvc.perform(get(requestPath)
                        .param("code", invalidCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmationWithoutCodeParameter() throws Exception {
        String requestPath = "/api/public/confirmation";

        mockMvc.perform(get(requestPath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}