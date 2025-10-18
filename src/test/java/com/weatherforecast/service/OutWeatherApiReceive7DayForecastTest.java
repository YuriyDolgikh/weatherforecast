package com.weatherforecast.service;

import com.weatherforecast.dto.forecastJSON.DailyForecast;
import com.weatherforecast.dto.forecastJSON.WeeklyForecast;
import com.weatherforecast.entity.Forecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class OutWeatherApiReceive7DayForecastTest {

    private static final String CITY_NAME = "London";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private OutWeatherApi outWeatherApi;
    private WeeklyForecast mockWeeklyForecast;
    private List<DailyForecast> mockDailyForecasts;

    @BeforeEach
    void setUp() {
        DailyForecast day1 = new DailyForecast("2025-01-01", 25.5, 15.2, 0.0);
        DailyForecast day2 = new DailyForecast("2025-01-02", 26.0, 16.0, 0.5);
        DailyForecast day3 = new DailyForecast("2025-01-03", 24.5, 14.8, 1.2);

        mockDailyForecasts = Arrays.asList(day1, day2, day3);

        mockWeeklyForecast = new WeeklyForecast();
        mockWeeklyForecast.setCityName(CITY_NAME);
        mockWeeklyForecast.setData(mockDailyForecasts);
    }

    @Test
    void testReceive7DayForecastAllIsOk() throws Exception {
        ResponseEntity<WeeklyForecast> responseEntity =
                new ResponseEntity<>(mockWeeklyForecast, HttpStatus.OK);

        when(restTemplate.getForEntity(any(URI.class), eq(WeeklyForecast.class)))
                .thenReturn(responseEntity);

        List<Forecast> result = outWeatherApi.receive7DayForecast(CITY_NAME);

        assertNotNull(result);
        assertEquals(3, result.size());

        Forecast firstForecast = result.get(0);
        assertEquals(CITY_NAME, firstForecast.getCityName());
        assertEquals(LocalDate.parse("2025-01-01"), firstForecast.getForecastDate());
        assertEquals("25.5", firstForecast.getMaxTemp());
        assertEquals("15.2", firstForecast.getMinTemp());
        assertEquals("0.0", firstForecast.getPrecip());
        assertNotNull(firstForecast.getCreateTime());

        result.forEach(forecast -> assertEquals(CITY_NAME, forecast.getCityName()));
    }

    @Test
    void testReceive7DayForecastEmptyList() throws Exception {
        WeeklyForecast emptyForecast = new WeeklyForecast();
        emptyForecast.setCityName(CITY_NAME);
        emptyForecast.setData(Collections.emptyList());

        ResponseEntity<WeeklyForecast> responseEntity =
                new ResponseEntity<>(emptyForecast, HttpStatus.OK);

        when(restTemplate.getForEntity(any(URI.class), eq(WeeklyForecast.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> outWeatherApi.receive7DayForecast(CITY_NAME));

        assertEquals("No weather data found in response for city: " + CITY_NAME,
                exception.getMessage());
    }

    @Test
    void testReceive7DayForecastWhenNullResponseBody() throws Exception {
        ResponseEntity<WeeklyForecast> responseEntity =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.getForEntity(any(URI.class), eq(WeeklyForecast.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> outWeatherApi.receive7DayForecast(CITY_NAME));

        assertEquals("No weather data found in response for city: " + CITY_NAME,
                exception.getMessage());
    }

    @Test
    void testReceive7DayForecastNoDataInResponse() throws Exception {
        WeeklyForecast nullDataForecast = new WeeklyForecast();
        nullDataForecast.setCityName(CITY_NAME);
        nullDataForecast.setData(null);

        ResponseEntity<WeeklyForecast> responseEntity =
                new ResponseEntity<>(nullDataForecast, HttpStatus.OK);

        when(restTemplate.getForEntity(any(URI.class), eq(WeeklyForecast.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> outWeatherApi.receive7DayForecast(CITY_NAME));

        assertEquals("No weather data found in response for city: " + CITY_NAME,
                exception.getMessage());
    }

    @Test
    void testReceive7DayForecastRestClientException() throws Exception {
        when(restTemplate.getForEntity(any(URI.class), eq(WeeklyForecast.class)))
                .thenThrow(new RestClientException("Connection timeout"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> outWeatherApi.receive7DayForecast(CITY_NAME));

        assertEquals("Error calling weather API", exception.getMessage());
        assertNotNull(exception.getCause());
        assertInstanceOf(RestClientException.class, exception.getCause());
    }
}