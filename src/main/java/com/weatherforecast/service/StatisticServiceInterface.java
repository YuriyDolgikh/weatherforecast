package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.user.UserResponseDto;

import java.util.List;

public interface StatisticServiceInterface {
    // for ADMIN only

    // get all users
    List<UserResponseDto> getAllUsers();

    // get sities added to favorites by all users
    List<CityResponseDto> getAllCitiesInFavorites();

    // get sities added to favorite by user
    List<CityResponseDto> getAllCitiesInFavoriteByUserId(Long userId);

    // the coldest city today
    CityResponseDto getColdestCity();

    // the warmest city today
    CityResponseDto getWarmestCity();

    // city with maximum precipitation today
    public CityResponseDto getCityWithMaxPrecipitation();
}
