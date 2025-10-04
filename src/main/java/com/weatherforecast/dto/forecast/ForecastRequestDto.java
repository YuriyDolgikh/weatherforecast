package com.weatherforecast.dto.forecast;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
/**
 * Request DTO for Forecast queries.
 * The client specifies the city and optionally the date.
 * Weather details (temperature, precipitation) will be fetched from the external API,
 * not provided by the client.
 */
@Data
@Builder
public class ForecastRequestDto {

    @NotNull(message = "City ID is required")
    private Long cityId;

    // Optional: user may request a forecast for a specific date within the next 7 days
    private LocalDate forecastDate;

}