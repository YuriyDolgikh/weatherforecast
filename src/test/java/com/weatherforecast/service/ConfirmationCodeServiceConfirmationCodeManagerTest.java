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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmationCodeServiceConfirmationCodeManagerTest {

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
    void testConfirmationCodeManagerCheckFullProcess() {
        User user = User.builder()
                .id(1L)
                .name("TestUser")
                .email("test@company.com")
                .build();

        confirmationCodeService.confirmationCodeManager(user);

        verify(confirmationCodeRepository).save(any(ConfirmationCode.class));
        verify(mailUtil).send(eq(user), any(String.class));
    }

    @Test
    void testConfirmationCodeManagerCallWithDifferentUsers() {
        User user1 = User.builder()
                .id(1L)
                .name("UserOne")
                .email("user1@company.com")
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("UserTwo")
                .email("user2@Company.com")
                .build();

        confirmationCodeService.confirmationCodeManager(user1);
        confirmationCodeService.confirmationCodeManager(user2);

        verify(confirmationCodeRepository, times(2)).save(any(ConfirmationCode.class));
        verify(mailUtil, times(1)).send(eq(user1), any(String.class));
        verify(mailUtil, times(1)).send(eq(user2), any(String.class));
    }
}