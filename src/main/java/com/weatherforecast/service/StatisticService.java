package com.weatherforecast.service;

import com.weatherforecast.controller.CityController;
import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.service.util.CityConverter;
import com.weatherforecast.service.util.ForecastConverter;
import com.weatherforecast.service.util.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.DocumentHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService implements StatisticServiceInterface {
    private final UserService userService;
    private final CityService cityService;
    private final CityConverter cityConverter;
    private final ForecastConverter forecastConverter;
    private final ForecastRepository forecastRepository;


    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        List<UserResponseDto> responceEmployeeDTOList = users.stream().map(user -> new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole())).toList();
        if (responceEmployeeDTOList.isEmpty()) {
            throw new NotFoundException(" Employees not found ");
        }
        return responceEmployeeDTOList;
    }

    @Override
    public Set<CityResponseDto> getAllCitiesInFavorites() {
        List<User> users = userService.getAllUsersFullDetails();
        if (users.isEmpty()) {
            throw new NotFoundException(" Users not found ");
        }
        Set<CityResponseDto> favoritesCities = new HashSet<>();


        for (User user : users) {

            for (City city : user.getCities()) {
                favoritesCities.add(cityConverter.toDto(city));
            }
        }

        if (favoritesCities.isEmpty()) {
            throw new NotFoundException(" Favorites cities not found ");
        }


        return favoritesCities;
    }

    @Override
    public Set<CityResponseDto> getAllCitiesInFavoriteByUserId(Long userId) {
        User user = userService.getUserByIdForAdmin(userId);

        if (user == null) {
            throw new NotFoundException(" User not found ");
        }

        Set<City> favoritesCities = user.getCities();

        if (favoritesCities.isEmpty()) {
            throw new NotFoundException(" Favorites cities not found ");
        }

        return favoritesCities.stream().map(city -> cityConverter.toDto(city)).collect(Collectors.toSet());
    }

    @Override
    public CityResponseDto getColdestCity() {
        List<Forecast> forecasts = forecastRepository.findByForecastDate(LocalDate.now());
        Forecast coldest = forecasts.
                stream()
                .filter(forecast -> forecast.getMinTemp() != null)
                .min(Comparator.comparingDouble(forecast -> Double.parseDouble(forecast.getMinTemp()))).orElseThrow(() -> new NotFoundException("Coldest City not found"));

        return cityService.getCityByName(coldest.getCityName());
    }

    @Override
    public CityResponseDto getWarmestCity() {
        List<Forecast> forecasts = forecastRepository.findByForecastDate(LocalDate.now());
        Forecast warmest = forecasts.
                stream()
                .filter(forecast -> forecast.getMaxTemp() != null)
                .max(Comparator.comparingDouble(forecast -> Double.parseDouble(forecast.getMaxTemp()))).orElseThrow(() -> new NotFoundException("Warmest City not found"));

        return cityService.getCityByName(warmest.getCityName());

    }

    @Override
    public CityResponseDto getCityWithMaxPrecipitation() {
        List<Forecast> forecasts = forecastRepository.findByForecastDate(LocalDate.now());
        Forecast maxPrecipitation = forecasts.
                stream()
                .filter(forecast -> forecast.getPrecip() != null)
                .max(Comparator.comparingDouble(forecast -> Double.parseDouble(forecast.getPrecip()))).orElseThrow(() -> new NotFoundException("City with max precipitation not found"));

        return cityService.getCityByName(maxPrecipitation.getCityName());

    }
}
