package com.weatherforecast.controller;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.repository.UserRepository;
import com.weatherforecast.security.service.JwtTokenProvider;
import com.weatherforecast.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
public class UserControllerDeleteUserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CommandLineRunner lineRunner;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .name("testUser")
                .email("testUser@company.com")
                .hashPassword(passwordEncoder.encode("pass123"))
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(now)
                .updateDate(now)
                .build();

        User savedUser = userRepository.save(user);

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code("someConfirmationCode")
                .user(savedUser)
                .expireDataTime(now.plusDays(1))
                .build();

        confirmationCodeRepository.save(confirmationCode);
    }

    @AfterEach
    void tearDown() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = {"USER"})
    void testdeleteUserWhenUserIsLoggedIn() throws Exception{

        String requestPath = "/api/user";

        mockMvc.perform(delete(requestPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testUser@company.com"))
                .andExpect(jsonPath("$.name").value("testUser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testDeleteUserWhenUserIsNotLoggedIn() throws Exception{

        String requestPath = "/api/user";

        mockMvc.perform(delete(requestPath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
