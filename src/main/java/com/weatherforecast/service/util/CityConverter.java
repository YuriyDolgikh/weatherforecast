package com.weatherforecast.service.util;

import com.weatherforecast.dto.city.CityRequestDto;
import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
                .build();
    }
    public List<CityResponseDto> toDtos(Collection<City> cities) {
        return cities.stream()
                .map(this::toDto)
                .toList();
    }
}