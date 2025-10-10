package com.weatherforecast.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WeeklyForecastResponseDto {
    private String cityName;
    private List<DailyForecastResponseDto> forecasts;
}