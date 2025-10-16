package com.weatherforecast.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TodayCityAverageWeatherResponseDto {
    private String cityName;
    private String avgTemp;
    private String precip;
}
