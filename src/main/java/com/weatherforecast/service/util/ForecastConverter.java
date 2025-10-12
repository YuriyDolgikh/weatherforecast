package com.weatherforecast.service.util;

import com.weatherforecast.dto.forecast.DailyForecastResponseDto;
import com.weatherforecast.dto.forecast.TodayCityAverageWeatherResponseDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.entity.Forecast;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converters for transforming between Forecast entity and Forecast DTOs.
 */
@Component
public class ForecastConverter {

    public DailyForecastResponseDto toDto(Forecast forecast) {
        return DailyForecastResponseDto.builder()
                .date(forecast.getForecastDate())
                .maxTemp(forecast.getMaxTemp())
                .minTemp(forecast.getMinTemp())
                .precip(forecast.getPrecip())
                .build();
    }

    public WeeklyForecastResponseDto toDto(String city, List<Forecast> entities) {
        List<DailyForecastResponseDto> forecastDtos = entities.stream()
                .map(entity -> toDto(entity))
                .collect(Collectors.toList());
        return new WeeklyForecastResponseDto(city, forecastDtos);
    }

    public TodayCityAverageWeatherResponseDto toDto(String city, Forecast forecast) {
        Double maxTemp = Double.parseDouble(forecast.getMaxTemp());
        Double minTemp = Double.parseDouble(forecast.getMinTemp());
        String avgTemp = String.valueOf((maxTemp + minTemp) / 2);
        return new TodayCityAverageWeatherResponseDto(city, avgTemp, forecast.getPrecip());
    }
}