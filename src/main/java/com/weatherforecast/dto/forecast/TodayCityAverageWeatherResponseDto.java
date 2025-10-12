package com.weatherforecast.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodayCityAverageWeatherResponseDto {
    private String cityName;
    private String avgTemp;
    private String precip;
}
