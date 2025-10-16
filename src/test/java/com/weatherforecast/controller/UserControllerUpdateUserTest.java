package com.weatherforecast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.dto.user.UserUpdateRequestDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
public class UserControllerUpdateUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommandLineRunner commandLineRunner;

    private User testUser;

    @BeforeEach
    void setUp() {

        LocalDateTime now = LocalDateTime.now();
        testUser = User.builder()
                .name("OldName")
                .email("testUser@company.com")
                .hashPassword("pass123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(now)
                .updateDate(now)
                .build();

        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserAllDetailsWhenUserIsLoggedIn() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setName("newName");
        updateRequest.setHashPassword("111222PPP");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithPartialData() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setName("newName");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"));
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithInvalidEmail() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("invalid@format");
        updateRequest.setName("NewFirstName");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithEmptyBody() throws Exception {
        // Пустой запрос
        String emptyRequestBody = "{}";

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserWhenUserIsNotLoggedIn() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setName("NewFirstName");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithShortPassword() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setHashPassword("A");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithBlankPassword() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setHashPassword(" ");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithNullPassword() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setHashPassword(null);

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithLongFirstName() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setName("A".repeat(30));

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithBlankFirstName() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setName(" ");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithNullFirstName() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("testUser@company.com");
        updateRequest.setName(null);

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testUpdateUserWithValidDataButDifferentUser() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        updateRequest.setEmail("mynewemail@company.com");
        updateRequest.setName("MyNewFirstName");

        String requestPath = "/api/user";

        mockMvc.perform(put(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}