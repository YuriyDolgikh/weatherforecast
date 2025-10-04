package com.weatherforecast.dto.forecast;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Response DTO for Forecast entity.
 * Contains weather information retrieved from an external API or database.
 */
@Data
@Builder
public class ForecastResponseDto {
    private Long id;
    private Long cityId;
    private String cityName;
    private String latitude;
    private String longitude;
    private LocalDate forecastDate;
    private String tempMax;
    private String tempMin;
    private String precipitation;
    private LocalDateTime updateDate;

}