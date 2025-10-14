package com.weatherforecast.service;

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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceGetUserByIdForAdminTest {

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
    void testGetUserByIdForAdminWhenUserExists() {
        User user = User.builder()
                .name("TestUser")
                .email("test@example.com")
                .hashPassword("password123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        User result = userService.getUserByIdForAdmin(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("TestUser", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(User.Role.USER, result.getRole());
        assertEquals(User.Status.NOT_CONFIRMED, result.getStatus());
    }

    @Test
    void testGetUserByIdForAdminWhenUserNotExists() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserByIdForAdmin(999L));

        assertEquals("User with id = " + "999" + " not found", exception.getMessage());
    }

    @Test
    void testGetUserByIdForAdminWhenIdIsNull() {

        assertThrows(InvalidDataAccessApiUsageException.class, () -> userService.getUserByIdForAdmin(null));
    }

    @Test
    void testGetUserByIdForAdminWithDifferentUserTypes() {
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

        User adminResult = userService.getUserByIdForAdmin(savedAdmin.getId());
        assertEquals(User.Role.ADMIN, adminResult.getRole());
        assertEquals(User.Status.CONFIRMED, adminResult.getStatus());
        assertEquals("AdminUser", adminResult.getName());
        assertEquals("admin@company.com", adminResult.getEmail());

        User notConfirmedResult = userService.getUserByIdForAdmin(savedNotConfirmed.getId());
        assertEquals(User.Role.USER, notConfirmedResult.getRole());
        assertEquals(User.Status.NOT_CONFIRMED, notConfirmedResult.getStatus());
        assertEquals("NotConfirmUser", notConfirmedResult.getName());
        assertEquals("notconfirmed@company.com", notConfirmedResult.getEmail());
    }

}