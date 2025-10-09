package com.weatherforecast.service;

import com.weatherforecast.dto.forecast.ForecastResponseDto;

import java.util.List;

public interface ForecastServiceInterface {
    // Get forecast for 7 days
    List<ForecastResponseDto> getForecastByCityName(String cityName);

    //Automatically update all cities forecast for 7 days
    void updateForecastForAllCities();
}
