package com.weatherforecast.service;

import com.weatherforecast.dto.forecastJSON.DailyForecast;
import com.weatherforecast.entity.Forecast;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class OutWeatherApiConvertToEntityTest {

    @InjectMocks
    private OutWeatherApi outWeatherApi;

    @Test
    void testConvertToEntitiesSuccessfulConversion() {
        String cityName = "London";
        List<DailyForecast> dailyForecasts = Arrays.asList(
                new DailyForecast("2024-01-01", 25.5, 15.2, 0.0),
                new DailyForecast("2024-01-02", 26.0, 16.0, 0.5),
                new DailyForecast("2024-01-03", 24.5, 14.8, 1.2)
        );

        List<Forecast> result = outWeatherApi.convertToEntities(cityName, dailyForecasts);

        assertNotNull(result);
        assertEquals(3, result.size());

        Forecast firstForecast = result.get(0);
        assertNull(firstForecast.getId());
        assertEquals(cityName, firstForecast.getCityName());
        assertEquals(LocalDate.parse("2024-01-01"), firstForecast.getForecastDate());
        assertEquals("25.5", firstForecast.getMaxTemp());
        assertEquals("15.2", firstForecast.getMinTemp());
        assertEquals("0.0", firstForecast.getPrecip());
        assertNotNull(firstForecast.getCreateTime());

        Forecast secondForecast = result.get(1);
        assertEquals("26.0", secondForecast.getMaxTemp());
        assertEquals("16.0", secondForecast.getMinTemp());
        assertEquals("0.5", secondForecast.getPrecip());

        result.forEach(forecast -> assertEquals(cityName, forecast.getCityName()));
    }

    @Test
    void testConvertToEntitiesEmptyList() {
        String cityName = "Paris";
        List<DailyForecast> emptyForecasts = Collections.emptyList();

        List<Forecast> result = outWeatherApi.convertToEntities(cityName, emptyForecasts);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertToEntitiesNullValuesInForecast() {
        String cityName = "Berlin";
        DailyForecast forecastWithNulls = new DailyForecast("2024-01-01", null, null, null);
        List<DailyForecast> dailyForecasts = Collections.singletonList(forecastWithNulls);

        List<Forecast> result = outWeatherApi.convertToEntities(cityName, dailyForecasts);

        assertNotNull(result);
        assertEquals(1, result.size());

        Forecast forecast = result.get(0);
        assertEquals("null", forecast.getMaxTemp());
        assertEquals("null", forecast.getMinTemp());
        assertEquals("null", forecast.getPrecip());
        assertEquals(LocalDate.parse("2024-01-01"), forecast.getForecastDate());
        assertEquals(cityName, forecast.getCityName());
    }

    @Test
    void testConvertToEntitiesDateTimeFormatParsing() {
        String cityName = "Sydney";
        List<DailyForecast> dailyForecasts = Arrays.asList(
                new DailyForecast("2024-12-31", 35.0, 25.0, 5.5),
                new DailyForecast("2025-01-01", 36.0, 26.0, 3.2)
        );

        List<Forecast> result = outWeatherApi.convertToEntities(cityName, dailyForecasts);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(LocalDate.parse("2024-12-31"), result.get(0).getForecastDate());
        assertEquals(LocalDate.parse("2025-01-01"), result.get(1).getForecastDate());
    }
}