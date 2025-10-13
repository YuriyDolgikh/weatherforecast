package com.weatherforecast.controller;

import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.TodayCityAverageWeatherResponseDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ForecastController {
    private final ForecastService forecastService;

    /**
     * Retrieves a 7-day weather forecast based on the provided request parameters.
     *
     * @param request the ForecastRequestDto containing location and other forecast parameters
     * @return a WeeklyForecastResponseDto containing the 7-day weather forecast
     */
    @PostMapping("/forecast")
    public WeeklyForecastResponseDto getWeatherForecast(@RequestBody ForecastRequestDto request) {
        return forecastService.get7DayForecast(request);
    }

    /**
     * Retrieves the average weather for all cities for today.
     *
     * @return a list of TodayCityAverageWeatherResponseDto objects containing today's average weather for each city
     */
    @GetMapping("/forecast")
    public List<TodayCityAverageWeatherResponseDto> getTodayCitiesAverageWeather() {
        return forecastService.getTodayCitiesAverageWeather();
    }
}
