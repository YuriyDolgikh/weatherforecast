package com.weatherforecast.dto.forecast;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ForecastRequestDto {
    @NotBlank(message = "City name is required")
    @Pattern(regexp = "^[A-Za-z._!-]+$", message = "Use only Latin letters")
    private String name;
}