package com.weatherforecast.service;

import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class StatisticServiceGetAllUsersTest {

    @MockBean
    private CommandLineRunner lineRunner;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    void dropDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void getAllUsers() {
        User user1 = User.builder()
                .name("User1")
                .email("user1@company.com")
                .hashPassword("password1")
                .role(User.Role.ADMIN)
                .status(User.Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("User2")
                .email("user2@company.com")
                .hashPassword("password2")
                .role(User.Role.USER)
                .status(User.Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("user1@company.com")));
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("user2@company.com")));
    }


    @Test
    void testGetAllUsersWhenNoUsers() {
        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


}