package com.weatherforecast.dto.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
/**
 * Response DTO for City entity.
 * Returned to clients when city data is requested.
 */
@Data
@AllArgsConstructor
@Builder
public class CityResponseDto {

    private Long id;

    private String name;

}