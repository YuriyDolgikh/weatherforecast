package com.weatherforecast.service;

import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceGetUserByEmailTest {

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
    void testGetUserByEmailWhenUserExists() {
        User user = User.builder()
                .name("TestUser")
                .email("test@example.com")
                .hashPassword("password123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
        String userEmail = "test@example.com";

        UserResponseDto result = userService.getUserByEmail(userEmail);

        assertNotNull(result);
        assertEquals(userEmail, result.getEmail());
        assertEquals("TestUser", result.getName());
        assertEquals(User.Role.USER.name(), result.getRole());
    }

    @Test
    void testGetUserByEmailWhenUserNotExists() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserByEmail("user@com.net"));

        assertEquals("User with email: " + "user@com.net" + " not found", exception.getMessage());
    }

    @Test
    void testGetUserByEmailWhenEmailIsNull() {

        assertThrows(NotFoundException.class, () -> userService.getUserByEmail(null));
    }

    @Test
    void testGetUserByEmailWhenEmailIsBlank() {

        assertThrows(NotFoundException.class, () -> userService.getUserByEmail("  "));
    }

    @Test
    void testGetUserByEmailWithDifferentUserTypes() {
        User adminUser = User.builder()
                .name("AdminUser")
                .email("admin@company.com")
                .hashPassword("admin123")
                .role(User.Role.ADMIN)
                .status(User.Status.CONFIRMED)
                .build();

        User notConfirmedUser = User.builder()
                .name("NotConfirmUser")
                .email("notconfirmed@company.com")
                .hashPassword("password123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .build();

        User savedAdmin = userRepository.save(adminUser);
        User savedNotConfirmed = userRepository.save(notConfirmedUser);

        UserResponseDto adminResult = userService.getUserByEmail(savedAdmin.getEmail());
        assertEquals(User.Role.ADMIN.name(), adminResult.getRole());
        assertEquals("AdminUser", adminResult.getName());
        assertEquals("admin@company.com", adminResult.getEmail());

        UserResponseDto notConfirmedResult = userService.getUserByEmail(savedNotConfirmed.getEmail());
        assertEquals(User.Role.USER.name(), notConfirmedResult.getRole());
        assertEquals("NotConfirmUser", notConfirmedResult.getName());
        assertEquals("notconfirmed@company.com", notConfirmedResult.getEmail());
    }

}