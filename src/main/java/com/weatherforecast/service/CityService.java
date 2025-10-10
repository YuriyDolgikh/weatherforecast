package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.service.util.CityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService implements CityServiceInterface{

    private final CityRepository cityRepository;
    private final CityConverter cityConverter;


    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public CityResponseDto getCityByName(String cityName) {
        City city =  cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City with name = " + cityName + " not found"));
        return cityConverter.toDto(city);
    }

    @Override
    public List<CityResponseDto> getCitiesByNameContainsIgnoreCase(String cityName) {
        return List.of();
    }

    @Override
    public List<CityResponseDto> addCityToFavorite(String cityName) {
        return List.of();
    }

    @Override
    public List<CityResponseDto> deleteCityFromFavorite(String cityName) {
        return List.of();
    }

    @Override
    public void saveCity(String cityName) {
        Optional<City> city = cityRepository.findByName(cityName);
        if (city.isEmpty()) {
            cityRepository.save(new City(null, cityName));
        }
    }
}
