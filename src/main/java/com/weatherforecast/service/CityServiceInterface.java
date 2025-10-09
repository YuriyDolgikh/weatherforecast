package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;

import java.util.List;

public interface CityServiceInterface {
    // Find cities by part of the name
    List<CityResponseDto> getCitiesByNameContains(String cityName);

    // Add a city to favorite (мах - 10)
    List<CityResponseDto> addCityToFavorite(String cityName);

    // Remove city from favorite
    List<CityResponseDto> deleteCityFromFavorite(String cityName);
}
