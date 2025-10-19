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
public class CityControllerAddCityToFavoriteTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @Test
    void testAddCityToFavoriteAllIsOk() {
        String cityName = "Kyiv";
        List<CityResponseDto> expectedUpdatedList = Arrays.asList(
                CityResponseDto.builder()
                        .id(1L)
                        .name("London")
                        .build(),
                CityResponseDto.builder()
                        .id(2L)
                        .name("Kyiv")
                        .build()
        );

        when(cityService.addCityToFavorite(cityName)).thenReturn(expectedUpdatedList);

        ResponseEntity<List<CityResponseDto>> response = cityController.addCityToFavorite(cityName);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedUpdatedList, response.getBody());
        verify(cityService, times(1)).addCityToFavorite(cityName);
    }

    @Test
    void testAddCityToFavoriteWhenCityIsNull() {
        when(cityService.addCityToFavorite(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> cityController.addCityToFavorite(null));
    }

    @Test
    void testAddCityToFavoriteWhenCityIsBlank() {
        when(cityService.addCityToFavorite(" ")).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> cityController.addCityToFavorite(" "));
    }
    }
