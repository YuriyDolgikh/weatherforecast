package com.weatherforecast.service.util;

import com.weatherforecast.dto.city.CityRequestDto;
import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityConverter {

    public City fromDto(CityRequestDto request){
        return City.builder()
                .name(request.getName())
                .build();
    }

    public CityResponseDto toDto(City city) {
        return CityResponseDto.builder()
                .id(city.getId())
                .name(city.getName())
                .latitude(city.getLatitude())
                .longitude(city.getLongitude())
                .build();
    }
    public List<CityResponseDto> fromCitiesToDtoList(List<City> cities) {
        return cities.stream()
                .map(this::toDto)
                .toList();
    }
}