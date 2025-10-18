package com.weatherforecast.service;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class OutWeatherApiCreateUrlTest {

    @InjectMocks
    private OutWeatherApi outWeatherApi;

    @Test
    void testCreateUrlAllIsOk() {
        String cityName = "London";

        String result = outWeatherApi.createUrl(cityName);

        assertNotNull(result);
        assertTrue(result.startsWith("https://api.weatherbit.io/v2.0/forecast/daily"));
        assertTrue(result.contains("city=London"));
        assertTrue(result.contains("key=1b7700a0a1da4b37bd47798363d96942"));
        assertTrue(result.contains("days=7"));

        String expectedUrl = "https://api.weatherbit.io/v2.0/forecast/daily?city=London&key=1b7700a0a1da4b37bd47798363d96942&days=7";
        assertEquals(expectedUrl, result);
    }

    @Test
    void testCreateUrlContainsAllRequiredParams() {
        String cityName = "Tokyo";

        String result = outWeatherApi.createUrl(cityName);

        assertNotNull(result);

        assertTrue(result.contains("city="));
        assertTrue(result.contains("key="));
        assertTrue(result.contains("days="));

        assertTrue(result.contains("key=1b7700a0a1da4b37bd47798363d96942"));
        assertTrue(result.contains("days=7"));
    }
}