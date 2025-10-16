package com.weatherforecast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.entity.User.Role;
import com.weatherforecast.entity.User.Status;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
public class PublicControllerRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @AfterEach
    void tearDown() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testRegisterNewUserWithValidData() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email("newuser@company.com")
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("newUser"))
                .andExpect(jsonPath("$.email").value("newuser@company.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testRegisterUserWithExistingEmail() throws Exception {
        User existingUser = User.builder()
                .name("user1")
                .email("existing@company.com")
                .hashPassword(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .status(Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        userRepository.save(existingUser);

        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email("existing@company.com")
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithShortName() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("ab")
                .email("user@company.com")
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithShortPassword() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email("user@company.com")
                .hashPassword("12345")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithInvalidEmail() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("userName")
                .email("invalid@email")
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithEmptyName() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("")
                .email("user@company.com")
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithEmptyEmail() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email("")
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithEmptyPassword() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email("user@company.com")
                .hashPassword("")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithNullEmail() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email(null)
                .hashPassword("password123")
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithNullPassword() throws Exception {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name("newUser")
                .email("user@company.com")
                .hashPassword(null)
                .build();

        String requestPath = "/api/public/register";

        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }
}