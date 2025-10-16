package com.weatherforecast.service;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.AlreadyExistException;
import com.weatherforecast.exception.BadRequestException;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceRegistrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        User testUser = User.builder()
                .name("testUser")
                .email("testUser@company.com")
                .hashPassword("Pass12345")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(now)
                .updateDate(now)
                .build();

        User savedUser = userRepository.save(testUser);

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code("someConfirmationCode")
                .user(savedUser)
                .expireDataTime(now.plusDays(1))
                .build();

        confirmationCodeRepository.save(confirmationCode);

    }

    @AfterEach
    void dropDatabase() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testWhenDuplicatedEmail() {
        UserRequestDto request = UserRequestDto.builder()
                .name("userOne")
                .email("testUser@company.com")
                .hashPassword("Pass111")
                .build();

        assertThrows(AlreadyExistException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenEmailHasWrongFormat() {
        UserRequestDto request = UserRequestDto.builder()
                .name("userOne")
                .email("testUsercompany.com")
                .hashPassword("Pass111")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenNameHasWrongFormat() {
        UserRequestDto request = UserRequestDto.builder()
                .name("u")
                .email("testUser1@company.com")
                .hashPassword("Pass111")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenEmailIsNull() {
        UserRequestDto request = UserRequestDto.builder()
                .name("userName")
                .email(null)
                .hashPassword("easrgf3223")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenNameIsNull() {
        UserRequestDto request = UserRequestDto.builder()
                .name(null)
                .email("testUser1@company.com")
                .hashPassword("easrgf3223")
                .build();

        assertThrows(BadRequestException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenPasswordIsNull() {
        UserRequestDto request = UserRequestDto.builder()
                .name("userName")
                .email("testUser1@company.com")
                .hashPassword(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenEmailIsBlank() {
        UserRequestDto request = UserRequestDto.builder()
                .name("userName")
                .email("  ")
                .hashPassword("easrgf3223")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenNameIsBlank() {
        UserRequestDto request = UserRequestDto.builder()
                .name("  ")
                .email("testUser1@company.com")
                .hashPassword("easrgf3223")
                .build();

        assertThrows(BadRequestException.class, () -> userService.registration(request));
    }

    @Test
    void testWhenPasswordIsBlank() {
        UserRequestDto request = UserRequestDto.builder()
                .name("userName")
                .email("testUser1@company.com")
                .hashPassword("  ")
                .build();

        assertThrows(BadRequestException.class, () -> userService.registration(request));
    }

    @Test
    void testGetAllUsers() {
        User newUser = User.builder()
                .id(null)
                .name("newUser")
                .email("newUser@comp.com")
                .hashPassword("dfg987fsgb")
                .role(User.Role.USER)
                .status(User.Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .cities(null)
                .build();
        userRepository.save(newUser);

        assert(userService.getAllUsers().size() == 2);
    }

}