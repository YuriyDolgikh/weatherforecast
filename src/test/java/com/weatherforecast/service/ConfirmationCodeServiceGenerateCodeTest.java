package com.weatherforecast.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationCodeServiceGenerateCodeTest {

    @InjectMocks
    private ConfirmationCodeService confirmationCodeService;

    @Test
    void testGenerateCodeReturnValidUUID() {
        String result = confirmationCodeService.generateCode();
        assertDoesNotThrow(() -> UUID.fromString(result));
    }

    @Test
    void testGenerateCodeReturnNotNullString() {
        String result = confirmationCodeService.generateCode();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGenerateCodeReturnUniqueValues() {
        String code1 = confirmationCodeService.generateCode();
        String code2 = confirmationCodeService.generateCode();
        String code3 = confirmationCodeService.generateCode();

        assertNotEquals(code1, code2);
        assertNotEquals(code1, code3);
        assertNotEquals(code2, code3);
    }

    @Test
    void testGenerateCodeCorrectUUIDFormat() {
        String result = confirmationCodeService.generateCode();

        assertEquals(36, result.length());
        assertEquals(5, result.split("-").length);
    }
}