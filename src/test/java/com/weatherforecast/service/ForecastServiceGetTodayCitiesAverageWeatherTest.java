package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.forecast.TodayCityAverageWeatherResponseDto;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.service.util.ForecastConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForecastServiceGetTodayCitiesAverageWeatherTest {

    @Mock
    private ForecastRepository repository;

    @Mock
    private CityService cityService;

    @Mock
    private ForecastConverter converter;

    @InjectMocks
    private ForecastService forecastService;

    @Test
    void getTodayCitiesAverageWeatherWhenCitiesExist() {
        List<CityResponseDto> favouriteCities = List.of(
                new CityResponseDto(1L, "Berlin"),
                new CityResponseDto(2L, "London"),
                new CityResponseDto(3L, "Paris")
        );

        Forecast berlinForecast = createForecast("Berlin");
        Forecast londonForecast = createForecast("London");
        Forecast parisForecast = createForecast("Paris");

        TodayCityAverageWeatherResponseDto BerlinDto = new TodayCityAverageWeatherResponseDto("Berlin", "20.0", "0.0");
        TodayCityAverageWeatherResponseDto londonDto = new TodayCityAverageWeatherResponseDto("London", "18.5", "1.2");
        TodayCityAverageWeatherResponseDto parisDto = new TodayCityAverageWeatherResponseDto("Paris", "22.3", "0.5");

        when(cityService.getCitiesByCurrentUser()).thenReturn(favouriteCities);
        when(repository.findByCityNameAndForecastDate("Berlin", LocalDate.now()))
                .thenReturn(Optional.of(berlinForecast));
        when(repository.findByCityNameAndForecastDate("London", LocalDate.now()))
                .thenReturn(Optional.of(londonForecast));
        when(repository.findByCityNameAndForecastDate("Paris", LocalDate.now()))
                .thenReturn(Optional.of(parisForecast));
        when(converter.toDto("Berlin", berlinForecast)).thenReturn(BerlinDto);
        when(converter.toDto("London", londonForecast)).thenReturn(londonDto);
        when(converter.toDto("Paris", parisForecast)).thenReturn(parisDto);

        List<TodayCityAverageWeatherResponseDto> result = forecastService.getTodayCitiesAverageWeather();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Berlin", result.get(0).getCityName());
        assertEquals("20.0", result.get(0).getAvgTemp());
        assertEquals("0.0", result.get(0).getPrecip());

        assertEquals("London", result.get(1).getCityName());
        assertEquals("18.5", result.get(1).getAvgTemp());
        assertEquals("1.2", result.get(1).getPrecip());

        assertEquals("Paris", result.get(2).getCityName());
        assertEquals("22.3", result.get(2).getAvgTemp());
        assertEquals("0.5", result.get(2).getPrecip());

        verify(cityService, times(1)).getCitiesByCurrentUser();
        verify(repository, times(3)).findByCityNameAndForecastDate(anyString(), eq(LocalDate.now()));
        verify(converter, times(3)).toDto(anyString(), any(Forecast.class));
    }

    @Test
    void getTodayCitiesAverageWeatherWhenNoCities() {
        List<CityResponseDto> favouriteCities = new ArrayList<>();

        when(cityService.getCitiesByCurrentUser()).thenReturn(favouriteCities);

        List<TodayCityAverageWeatherResponseDto> result = forecastService.getTodayCitiesAverageWeather();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(cityService, times(1)).getCitiesByCurrentUser();
        verify(repository, never()).findByCityNameAndForecastDate(anyString(), any(LocalDate.class));
        verify(converter, never()).toDto(anyString(), any(Forecast.class));
    }

    private Forecast createForecast(String cityName) {
        Forecast forecast = new Forecast();
        forecast.setCityName(cityName);
        forecast.setForecastDate(LocalDate.now());
        forecast.setMaxTemp(String.valueOf(25.0));
        forecast.setMinTemp(String.valueOf(15.0));
        forecast.setPrecip(String.valueOf(0.0));
        forecast.setCreateTime(LocalDateTime.now());
        return forecast;
    }
}
