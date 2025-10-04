package com.weatherforecast.dto.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
/**
 * Request DTO for creating or updating a City.
 * Used when adding cities to the database (typically by ADMIN).
 */
@Data
@Builder
public class CityRequestDto {

    @NotBlank(message = "City name is required")
    @Size(min = 3, max = 20, message = "City name must be between 3 and 20 characters")
    private String name;

    @NotBlank(message = "Latitude is required")
    private String latitude;

    @NotBlank(message = "Longitude is required")
    private String longitude;
}