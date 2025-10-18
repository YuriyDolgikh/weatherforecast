package com.weatherforecast.service;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.service.mail.MailUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationCodeServiceSaveConfirmationCodeTest {

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
    void testSaveConfirmationCodeCallRepositorySave() {
        String code = "test-uuid-code";
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@company.com")
                .build();

        confirmationCodeService.saveConfirmationCode(code, user);

        verify(confirmationCodeRepository).save(any(ConfirmationCode.class));
    }

    @Test
    void testSaveConfirmationCodeSaveWithCorrectCodeAndUser() {
        String codeForSave = "test-uuid-123";
        User userForSave = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@company.com")
                .build();

        ConfirmationCode[] codes = new ConfirmationCode[1];

        when(confirmationCodeRepository.save(any(ConfirmationCode.class)))
                .then(invocationOnMock -> {
                    codes[0] = invocationOnMock.getArgument(0);
                    return codes[0];
                });

        confirmationCodeService.saveConfirmationCode(codeForSave, userForSave);

        verify(confirmationCodeRepository).save(any(ConfirmationCode.class));
        assert codes[0] != null;
        assert codes[0].getCode().equals(codeForSave);
        assert codes[0].getUser().equals(userForSave);
        assert !codes[0].isConfirmed();
        assert codes[0].getExpireDataTime() != null;
        LocalDateTime dateForTest = LocalDateTime.now().plusDays(179);
        assert codes[0].getExpireDataTime().isAfter(dateForTest);
    }

    @Test
    void testSaveConfirmationCodeCallRepositoryOnlyOnce() {
        String code = "test-code";
        User user = User.builder().id(1L).email("test@company.com").build();

        confirmationCodeService.saveConfirmationCode(code, user);

        verify(confirmationCodeRepository, times(1)).save(any(ConfirmationCode.class));
    }
}