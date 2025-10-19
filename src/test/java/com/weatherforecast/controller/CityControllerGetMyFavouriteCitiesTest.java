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
public class CityControllerGetMyFavouriteCitiesTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @Test
    void testGetMyFavouriteCitiesAllIsOk() {
        List<CityResponseDto> expectedFavouriteCities = Arrays.asList(
                CityResponseDto.builder()
                        .id(1L)
                        .name("Kyiv")
                        .build(),
                CityResponseDto.builder()
                        .id(2L)
                        .name("London")
                        .build()
        );

        when(cityService.getCitiesByCurrentUser()).thenReturn(expectedFavouriteCities);

        ResponseEntity<List<CityResponseDto>> response = cityController.getMyFavouriteCities();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedFavouriteCities, response.getBody());

        verify(cityService, times(1)).getCitiesByCurrentUser();
    }

    @Test
    void testGetMyFavouriteCitiesWhenNoFavouriteCities() {
        List<CityResponseDto> expectedFavouriteCities = List.of();
        when(cityService.getCitiesByCurrentUser()).thenReturn(expectedFavouriteCities);

        ResponseEntity<List<CityResponseDto>> response = cityController.getMyFavouriteCities();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(cityService, times(1)).getCitiesByCurrentUser();
    }
}