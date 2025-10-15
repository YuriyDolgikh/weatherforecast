package com.weatherforecast.service;

import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.dto.user.UserUpdateRequestDto;
import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.BadRequestException;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceUpdateUserTest {

    @MockBean
    private CommandLineRunner lineRunner;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();


        User testUser = User.builder()
                .name("testUser")
                .email("testUser@company.com")
                .hashPassword("pass123")
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
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserUpdateName() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("testUser@company.com")
                .name("newUserName")
                .build();

        UserResponseDto response = userService.updateUser(updateRequest);
        assertEquals("testUser@company.com", response.getEmail());
        assertEquals("newUserName", response.getName());

        User updatedUser = userRepository.findByEmail("testUser@company.com").orElseThrow();
        assertEquals("newUserName", updatedUser.getName());
        assertEquals("pass123", updatedUser.getHashPassword());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserUpdatePassword() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("testUser@company.com")
                .hashPassword("123456789")
                .build();

        UserResponseDto response = userService.updateUser(updateRequest);
        assertEquals("testUser@company.com", response.getEmail());
        assertEquals("testUser", response.getName());

        User updatedUser = userRepository.findByEmail("testUser@company.com").orElseThrow();
        assertEquals("123456789", updatedUser.getHashPassword());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserWhenEmailIsNull() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email(null)
                .name("newUserName")
                .build();

        Exception exception = assertThrows(BadRequestException.class, () -> userService.updateUser(updateRequest));
        assertEquals("Email must be provided to update user", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserWhenEmailIsBlank() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("   ")
                .name("newUserName")
                .build();

        Exception exception = assertThrows(BadRequestException.class, () -> userService.updateUser(updateRequest));
        assertEquals("Email must be provided to update user", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserWhenUserNonFound() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("someEmail@company.com")
                .name("newUserName")
                .build();

        Exception exception = assertThrows(NotFoundException.class, () -> userService.updateUser(updateRequest));
        assertEquals("User with email: someEmail@company.com not found", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserWhenUpdateAnotherUser() {

        User userForUpdate = User.builder()
                .name("testUser")
                .email("anotherEmail@company.com")
                .hashPassword("12342345")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .build();

        userRepository.save(userForUpdate);

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("anotherEmail@company.com")
                .name("newUserName")
                .build();

        Exception exception = assertThrows(BadRequestException.class, () -> userService.updateUser(updateRequest));
        assertEquals("You can't update another user", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserUpdateUpdateAllFieldsWhenPasswordIsNull() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("testUser@company.com")
                .name("newUserName")
                .hashPassword(null)
                .build();

        UserResponseDto response = userService.updateUser(updateRequest);

        assertEquals("newUserName", response.getName());

        User updatedUser = userRepository.findByEmail("testUser@company.com").orElseThrow();
        assertEquals("pass123", updatedUser.getHashPassword());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserUpdateUpdateAllFieldsWhenPasswordIsBlank() {

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("testUser@company.com")
                .name("newUserName")
                .hashPassword("             ")
                .build();

        UserResponseDto response = userService.updateUser(updateRequest);

        assertEquals("newUserName", response.getName());

        User updatedUser = userRepository.findByEmail("testUser@company.com").orElseThrow();
        assertEquals("pass123", updatedUser.getHashPassword());
    }

    @Test
    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testUpdateUserSetUpdateDate() {

        User userForUpdate = userRepository.findByEmail("testUser@company.com").orElseThrow();
        userForUpdate.setUpdateDate(LocalDateTime.now().minusDays(1));
        User updatedUser = userRepository.save(userForUpdate);

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .email("testUser@company.com")
                .name("newUserName")
                .build();

        UserResponseDto response = userService.updateUser(updateRequest);

        assertEquals("newUserName", response.getName());

        User userFromRepository = userRepository.findByEmail("testUser@company.com").orElseThrow();
        assertTrue(updatedUser.getUpdateDate().isBefore(userFromRepository.getUpdateDate()));
    }


}