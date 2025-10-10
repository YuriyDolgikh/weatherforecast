package com.weatherforecast.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for Forecast queries.
 * The client specifies the city and optionally the date.
 * Weather details (temperature, precipitation) will be fetched from the external API,
 * not provided by the client.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastRequestDto {

//    @NotNull(message = "City name is required")
    private String name;

}