package com.weatherforecast.service;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class ConfirmationCodeServiceChangeConfirmationStatusByCodeTest {

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

    @InjectMocks
    private ConfirmationCodeService confirmationCodeService;

    @AfterEach
    void tearDown() {
        confirmationCodeRepository.deleteAll();
    }

    @Test
    void testChangeConfirmationStatusByCodeFullProcessOk() {
        // Given
        String code = "valid-code";
        User expectedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code(code)
                .user(expectedUser)
                .isConfirmed(false)
                .build();

        when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        User result = confirmationCodeService.changeConfirmationStatusByCode(code);

        assertEquals(expectedUser, result);
        assertTrue(confirmationCode.isConfirmed());
        verify(confirmationCodeRepository).save(confirmationCode);
    }

    @Test
    void testChangeConfirmationStatusByCodeWhenCodeNotFound() {
        String invalidCode = "invalid-code";
        when(confirmationCodeRepository.findByCode(invalidCode)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> confirmationCodeService.changeConfirmationStatusByCode(invalidCode));

        assertTrue(exception.getMessage().contains(invalidCode));
    }

    @Test
    void testChangeConfirmationStatusByCodeReturnCorrectUser() {
        String code = "test-code";
        User expectedUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code(code)
                .user(expectedUser)
                .isConfirmed(false)
                .build();

        when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        User result = confirmationCodeService.changeConfirmationStatusByCode(code);

        assertEquals(expectedUser, result);
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getEmail(), result.getEmail());
    }
}