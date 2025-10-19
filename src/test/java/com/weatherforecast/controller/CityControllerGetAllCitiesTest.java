package com.weatherforecast.controller;
import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.service.CityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityControllerGetAllCitiesTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @Test
    void testGetAllCitiesAllIsOk() {
        List<CityResponseDto> expectedCities = Arrays.asList(
                CityResponseDto.builder()
                        .id(1L)
                        .name("Kyiv")
                        .build(),
                CityResponseDto.builder()
                        .id(2L)
                        .name("London")
                        .build(),
                CityResponseDto.builder()
                        .id(3L)
                        .name("New York")
                        .build()
        );

        when(cityService.getAllCities()).thenReturn(expectedCities);

        ResponseEntity<List<CityResponseDto>> response = cityController.getAllCities();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals(expectedCities, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(cityService, times(1)).getAllCities();
    }

    @Test
    void testGetAllCitiesWhenNoCities() {
        List<CityResponseDto> expectedCities = List.of();
        when(cityService.getAllCities()).thenReturn(expectedCities);

        ResponseEntity<List<CityResponseDto>> response = cityController.getAllCities();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(cityService, times(1)).getAllCities();
    }

    @Test
    void shouldThrowException_WhenServiceFails() {
        when(cityService.getAllCities()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> cityController.getAllCities());
        verify(cityService, times(1)).getAllCities();

    }
}