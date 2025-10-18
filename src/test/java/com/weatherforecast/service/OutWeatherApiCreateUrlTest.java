package com.weatherforecast.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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