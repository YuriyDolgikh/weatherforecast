package com.weatherforecast.service;

import com.weatherforecast.dto.forecast.DailyForecastResponseDto;
import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.service.util.ForecastConverter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class ForecastServiceGet7DayForecastTest {

    @Mock
    private ForecastRepository repository;

    @Mock
    private OutWeatherApi outWeatherApi;

    @Mock
    private CityService cityService;

    @Mock
    private ForecastConverter converter;

    @InjectMocks
    private ForecastService forecastService;

    @Test
    void get7DayForecastWhenDataExistsInDbAndIsFreshReturnFromDb() {
        String cityName = "Berlin";
        ForecastRequestDto forecastRequestDto = new ForecastRequestDto(cityName);

        List<Forecast> forecastsFromDb = createForecastList(7);
        List<DailyForecastResponseDto> forecastsFromDbDtos = createForecastListDtos(forecastsFromDb);
        WeeklyForecastResponseDto expectedResponse = new WeeklyForecastResponseDto(cityName, forecastsFromDbDtos);

        when(repository.findByCityNameAndCreateTimeAfterOrderByForecastDateAsc(
                eq(cityName), any(LocalDateTime.class))).thenReturn(forecastsFromDb);
        when(converter.toDto(cityName, forecastsFromDb)).thenReturn(expectedResponse);

        WeeklyForecastResponseDto result = forecastService.get7DayForecast(forecastRequestDto);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(outWeatherApi, never()).receive7DayForecast(anyString());
    }

    @Test
    void get7DayForecastWhenDataExistsButIncompleteThenFetchFromApi() {
        String cityName = "London";
        ForecastRequestDto request = new ForecastRequestDto(cityName);

        List<Forecast> forecastsFromDb = createForecastList(5);
        List<Forecast> forecastsFromApi = createForecastList(7);
        List<DailyForecastResponseDto> forecastsFromApiDtos = createForecastListDtos(forecastsFromApi);
        WeeklyForecastResponseDto expectedResponse = new WeeklyForecastResponseDto(cityName, forecastsFromApiDtos);

        when(repository.findByCityNameAndCreateTimeAfterOrderByForecastDateAsc(
                eq(cityName), any(LocalDateTime.class))).thenReturn(forecastsFromDb);
        when(outWeatherApi.receive7DayForecast(cityName)).thenReturn(forecastsFromApi);
        when(converter.toDto(cityName, forecastsFromApi)).thenReturn(expectedResponse);

        WeeklyForecastResponseDto result = forecastService.get7DayForecast(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(repository, times(1)).deleteByCityName(cityName);
        verify(repository, times(1)).saveAll(forecastsFromApi);
        assertEquals(7, forecastsFromApi.size());
    }

    private List<Forecast> createForecastList(int count) {
        List<Forecast> forecasts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Forecast forecast = new Forecast();

            forecast.setId((long) (i + 1));
            forecast.setCityName("Berlin");
            forecast.setForecastDate(LocalDate.now().plusDays(i));
            forecast.setMaxTemp(String.valueOf(25.0 + i));
            forecast.setMinTemp(String.valueOf(15.0 + i));
            forecast.setPrecip(String.valueOf(0.0 + i));
            forecast.setCreateTime(LocalDateTime.now());

            forecasts.add(forecast);
        }
        return forecasts;
    }

    private List<DailyForecastResponseDto> createForecastListDtos(List<Forecast> forecastsFromDb) {
        List<DailyForecastResponseDto> forecastsDtos = new ArrayList<>();
        for(Forecast forecast : forecastsFromDb) {
            DailyForecastResponseDto forecastDto = new DailyForecastResponseDto();
            forecastDto.setDate(forecast.getForecastDate());
            forecastDto.setMaxTemp(forecast.getMaxTemp());
            forecastDto.setMinTemp(forecast.getMinTemp());
            forecastDto.setPrecip(forecast.getPrecip());
            forecastsDtos.add(forecastDto);
        }
        return forecastsDtos;
    }

}