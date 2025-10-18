package com.weatherforecast.service;

import com.weatherforecast.entity.User;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.service.mail.MailUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationCodeServiceSendCodeByEmailTest {

    String linkPrefix = "http://localhost:8080/api/public/confirmation?code=";

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Mock
    private MailUtil mailUtil;

    @InjectMocks
    private ConfirmationCodeService confirmationCodeService;

    @AfterEach
    void tearDown() {
        confirmationCodeRepository.deleteAll();
    }

    @Test
    void testSendCodeByEmailIsCallMailUtilWithCorrectParameters() {
        String code = "test-uuid-code-12345678";
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@company.com")
                .build();

        String expectedLink = linkPrefix + code;

        confirmationCodeService.sendCodeByEmail(code, user);

        verify(mailUtil).send(eq(user), eq(expectedLink));
    }

    @Test
    void testSendCodeByEmailCheckNotCallRepository() {
        String code = "test-code";
        User user = User.builder().id(1L).email("test@company.com").build();

        confirmationCodeService.sendCodeByEmail(code, user);

        verify(confirmationCodeRepository, never()).save(any());
        verify(confirmationCodeRepository, never()).findByCode(any());
        verify(confirmationCodeRepository, never()).findByUser(any());
    }

    @Test
    void testSendCodeByEmailCallMailUtil() {
        String code = "test-code";
        User user = User.builder().id(1L).email("test@company.com").build();

        confirmationCodeService.sendCodeByEmail(code, user);

        verify(mailUtil, times(1)).send(any(User.class), any(String.class));
    }
}