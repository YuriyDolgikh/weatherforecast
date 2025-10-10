package com.weatherforecast.controller;

import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ForecastController {
    private final ForecastService forecastService;

    @PostMapping("/forecast")
    public WeeklyForecastResponseDto getWeatherData(@RequestBody ForecastRequestDto request) {
        return forecastService.get7DayForecast(request);
    }
}
