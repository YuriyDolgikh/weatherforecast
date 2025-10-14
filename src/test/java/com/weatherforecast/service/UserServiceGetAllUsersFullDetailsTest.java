package com.weatherforecast.service;

import com.weatherforecast.entity.User;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceGetAllUsersFullDetailsTest {

    @MockBean
    private CommandLineRunner lineRunner;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void dropDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void testGetAllUsersFullDetailsWhenUsersExist() {
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
                .status(User.Status.NOT_CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> result = userService.getAllUsersFullDetails();

        assertNotNull(result);
        assertEquals(2, result.size());

        User savedUser1 = result.get(0);
        User savedUser2 = result.get(1);

        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("user1@company.com")));
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("user2@company.com")));

        assertEquals("ADMIN", savedUser1.getRole().name());
        assertEquals("USER", savedUser2.getRole().name());

        assertEquals("CONFIRMED", savedUser1.getStatus().name());
        assertEquals("NOT_CONFIRMED", savedUser2.getStatus().name());
    }

    @Test
    void testGetAllUsersFullDetailsWhenUsersNotExists() {
        List<User> result = userService.getAllUsersFullDetails();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllUsersFullDetailsWithCorrectData() {
        String hashedPassword = passwordEncoder.encode("pass123");
        User user = User.builder()
                .name("TestUser")
                .email("test@company.com")
                .hashPassword(hashedPassword)
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .cities(new HashSet<>())
                .build();

        userRepository.save(user);

        List<User> result = userService.getAllUsersFullDetails();

        User userFromResult = result.get(0);
        assertEquals("TestUser", userFromResult.getName());
        assertEquals("test@company.com", userFromResult.getEmail());
        assertEquals(hashedPassword, userFromResult.getHashPassword());
        assertEquals(User.Role.USER, userFromResult.getRole());
        assertEquals(User.Status.NOT_CONFIRMED, userFromResult.getStatus());
    }

}