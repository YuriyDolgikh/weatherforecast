package com.weatherforecast.service.util;

import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.ForecastResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import org.springframework.stereotype.Component;

import java.util.List;
import java.time.LocalDate;

/**
 * Converter for transforming between Forecast entity and Forecast DTOs.
 */
@Component
public class ForecastConverter {

    public Forecast fromDto(ForecastRequestDto request, City city){
        return Forecast.builder()
                .city(city)
                .forecastDate(request.getForecastDate())
                .build();
    }

    public ForecastResponseDto toDto(Forecast forecast) {
        return ForecastResponseDto.builder()
                .id(forecast.getId())
                .cityId(forecast.getCity().getId())
                .cityName(forecast.getCity().getName())
                .latitude(forecast.getCity().getLatitude())
                .longitude(forecast.getCity().getLongitude())
                .forecastDate(forecast.getForecastDate())
                .tempMax(forecast.getTempMax())
                .tempMin(forecast.getTempMin())
                .precipitation(forecast.getPrecipitation())
                .updateDate(forecast.getUpdateDateTime())
                .build();
    }
    public List<ForecastResponseDto> fromForecastsToDtoList(List<Forecast> forecasts) {
        return forecasts.stream()
                .map(this::toDto)
                .toList();
    }

}