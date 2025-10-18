package com.weatherforecast.service;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationCodeServiceFindCodesByUserTest {

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

    @InjectMocks
    private ConfirmationCodeService confirmationCodeService;

    @AfterEach
    void tearDown() {
        confirmationCodeRepository.deleteAll();
    }

    @Test
    void testFindCodesByUserIfPresent() {
        User user = User.builder().id(1L).email("test@example.com").build();
        List<ConfirmationCode> expectedCodes = Arrays.asList(
                ConfirmationCode.builder().id(1L).user(user).build(),
                ConfirmationCode.builder().id(2L).user(user).build()
        );

        when(confirmationCodeRepository.findByUser(user)).thenReturn(expectedCodes);

        List<ConfirmationCode> result = confirmationCodeService.findCodesByUser(user);

        assertEquals(expectedCodes, result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindCodesByUserWhenNoCodesFound() {
        User user = User.builder().id(1L).email("test@example.com").build();
        when(confirmationCodeRepository.findByUser(user)).thenReturn(Collections.emptyList());

        List<ConfirmationCode> result = confirmationCodeService.findCodesByUser(user);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindCodesByUserWhenUserIsNull() {
        User user = null;
        assertThrows(IllegalArgumentException.class, () -> confirmationCodeService.findCodesByUser(user));
    }

}