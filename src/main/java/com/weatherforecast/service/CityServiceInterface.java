package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;

import java.util.List;

public interface CityServiceInterface {

    // Get all cities from DB
    List<CityResponseDto> getAllCities();

    // Find a city by name
    CityResponseDto getCityByName(String cityName);

    // Find cities by part of the name
    List<CityResponseDto> getCitiesByNameContainsIgnoreCase(String cityName);

    // Add a city to favorite (мах - 10)
    List<CityResponseDto> addCityToFavorite(String cityName);

    // Remove city from favorite
    List<CityResponseDto> deleteCityFromFavorite(String cityName);

    // Add city to DB
    void saveCity(String cityName);
}
