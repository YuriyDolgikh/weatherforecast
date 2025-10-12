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

    @PostMapping("/forecast")
    public WeeklyForecastResponseDto getWeatherForecast(@RequestBody ForecastRequestDto request) {
        return forecastService.get7DayForecast(request);
    }

    @GetMapping("/forecast")
    public List<TodayCityAverageWeatherResponseDto> getTodayCitiesAverageWeather() {
        return forecastService.getTodayCitiesAverageWeather();
    }
}
