package com.weatherforecast.service;

import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;

public interface ForecastServiceInterface {

    // Get forecast for 7 days
    WeeklyForecastResponseDto get7DayForecast(ForecastRequestDto request);

    //Automatically update all cities forecast for 7 days by scheduler
    void updateForecastForAllCitiesFromDatabase();
}
