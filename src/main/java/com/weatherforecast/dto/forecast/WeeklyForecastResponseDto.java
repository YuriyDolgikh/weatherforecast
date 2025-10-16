package com.weatherforecast.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyForecastResponseDto {
    private String cityName;
    private List<DailyForecastResponseDto> forecasts;
}