package com.weatherforecast.service;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ConfirmationCodeRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceConfirmationEmailTest {

    @MockBean
    private CommandLineRunner lineRunner;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void dropDatabase() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testConfirmationEmailSuccess() {
        User user = User.builder()
                .name("TestUser")
                .email("test@example.com")
                .hashPassword(passwordEncoder.encode("password123"))
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .cities(new HashSet<>())
                .build();

        User savedUser = userRepository.save(user);

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code("validConfirmationCode")
                .user(savedUser)
                .isConfirmed(false)
                .expireDataTime(LocalDateTime.now().plusHours(24))
                .build();

        confirmationCodeRepository.save(confirmationCode);

        String result = userService.confirmationEmail("validConfirmationCode");

        assertEquals("Email test@example.com is successfully confirmed", result);

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals(User.Status.CONFIRMED, updatedUser.getStatus());

        ConfirmationCode updatedCode = confirmationCodeRepository.findByCode("validConfirmationCode").get();
        assertTrue(updatedCode.isConfirmed());
    }

    @Test
    void testConfirmationEmailInvalidCode() {
        String invalidCode = "TutKakoiToBred";
        assertThrows(NotFoundException.class, () -> userService.confirmationEmail(invalidCode));
    }

    @Test
    void testConfirmationEmailNullCode() {
        assertThrows(NotFoundException.class, () -> userService.confirmationEmail(null));
    }

    @Test
    void testConfirmationEmailEmptyCode() {
        assertThrows(NotFoundException.class, () -> userService.confirmationEmail(" "));
    }

    @Test
    void testConfirmationEmailOneTransaction() {
        User user = User.builder()
                .name("someUser")
                .email("someUser@example.com")
                .hashPassword("password123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .cities(new HashSet<>())
                .build();

        User savedUser = userRepository.save(user);

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code("validConfirmationCode")
                .user(savedUser)
                .isConfirmed(false)
                .expireDataTime(LocalDateTime.now().plusHours(24))
                .build();

        confirmationCodeRepository.save(confirmationCode);

        String result = userService.confirmationEmail("validConfirmationCode");

        assertEquals("Email someUser@example.com is successfully confirmed", result);

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        ConfirmationCode updatedCode = confirmationCodeRepository.findByCode("validConfirmationCode").get();

        assertEquals(User.Status.CONFIRMED, updatedUser.getStatus());
        assertTrue(updatedCode.isConfirmed());
    }

}