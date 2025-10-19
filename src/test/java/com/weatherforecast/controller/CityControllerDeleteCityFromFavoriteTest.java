package com.weatherforecast.controller;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.exception.NotFoundException;
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
public class CityControllerDeleteCityFromFavoriteTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;


    @Test
    void testDeleteCityFromFavoriteAllIsOk() {
        String cityName = "Kyiv";
        List<CityResponseDto> expectedUpdatedList = Arrays.asList(
                CityResponseDto.builder().id(1L).name("London").build(),
                CityResponseDto.builder().id(3L).name("Paris").build()
        );

        when(cityService.deleteCityFromFavorite(cityName)).thenReturn(expectedUpdatedList);

        ResponseEntity<List<CityResponseDto>> response = cityController.deleteCityFromFavorite(cityName);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedUpdatedList, response.getBody());

        verify(cityService, times(1)).deleteCityFromFavorite(cityName);
    }

    @Test
    void testDeleteCityFromFavoriteWhenCityNameIsWrong() {
        String cityName = "London";
        List<CityResponseDto> expectedUpdatedList = Arrays.asList(
                CityResponseDto.builder().id(1L).name("Paris").build()
        );

        when(cityService.deleteCityFromFavorite(cityName))
                .thenThrow(new NotFoundException("City with name = " + cityName + " is not found in user favorite cities"));

        Exception exception = assertThrows(NotFoundException.class, () -> cityController.deleteCityFromFavorite(cityName));
        assertEquals("City with name = London is not found in user favorite cities", exception.getMessage());
        verify(cityService, times(1)).deleteCityFromFavorite(cityName);
    }

    @Test
    void testDeleteCityFromFavoriteWhenCityNameIsNull() {
        when(cityService.deleteCityFromFavorite(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> cityController.deleteCityFromFavorite(null));
    }

    @Test
    void testDeleteCityFromFavoriteWhenCityNameIsBlank() {
        when(cityService.deleteCityFromFavorite(" ")).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> cityController.deleteCityFromFavorite(" "));
    }
}
