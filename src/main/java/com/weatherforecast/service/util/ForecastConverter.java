package com.weatherforecast.service.util;

import com.weatherforecast.dto.forecast.ForecastResponseDto;
import com.weatherforecast.entity.Forecast;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter for transforming between Forecast entity and Forecast DTOs.
 */
@Component
public class ForecastConverter {

    public ForecastResponseDto toDto(Forecast forecast) {
        return ForecastResponseDto.builder()
                .id(forecast.getId())
                .cityName(forecast.getCity().getName())
                .forecastDate(forecast.getForecastDate())
                .tempMax(forecast.getTempMax())
                .tempMin(forecast.getTempMin())
                .precipitation(forecast.getPrecipitation())
                .build();
    }

    public List<ForecastResponseDto> fromForecastsToDtoList(List<Forecast> forecasts) {
        return forecasts.stream()
                .map(this::toDto)
                .toList();
    }

}