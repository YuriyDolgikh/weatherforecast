package com.weatherforecast.controller;

import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.TodayCityAverageWeatherResponseDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.service.ForecastService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "Weather Forecast", description = "API for getting weather forecast")
public class ForecastController {
    private final ForecastService forecastService;

    @PostMapping("/forecast")
    public WeeklyForecastResponseDto getWeatherForecast(@RequestBody ForecastRequestDto request) {
        return forecastService.get7DayForecast(request);
    }

    @GetMapping("/forecast")
    public List<TodayCityAverageWeatherResponseDto> getTodayCitiesAverageWeather() {
        return forecastService.getTodayCitiesAverageWeather();
    }
}
