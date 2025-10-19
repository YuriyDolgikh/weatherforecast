package com.weatherforecast.controller;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StatisticService statisticService;

    /**
     * Retrieves all users
     *
     * @return a list of users as UserResponseDto objects
     */
    @GetMapping("/findAll")
    ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(statisticService.getAllUsers());
    }

    /**
     * Retrieves all favorites cities from all users
     *
     * @return a set of cities as CityResponseDto
     */
    @GetMapping("/allFavoritesCities")
    ResponseEntity<Set<CityResponseDto>> findAllFavoritesCitiesByAllUsers() {
        return ResponseEntity.ok(statisticService.getAllCitiesInFavorites());
    }

    /**
     * Retrieves all favorite cities for a specific user
     *
     * @param id the ID of the user whose favorite cities should be retrieved
     * @return a Set cities from user as CityResponseDto
     */
    @GetMapping("/allFavoritesCitiesByUser/{id}")
    ResponseEntity<Set<CityResponseDto>> findAllFavoritesCitiesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(statisticService.getAllCitiesInFavoriteByUserId(id));
    }

    /**
     * Retrieves coldest city based on the available weather data.
     *
     * @return ResponseEntity containing the CityResponseDto of the coldest city
     */
    @GetMapping("/coldest")
    ResponseEntity<CityResponseDto> findColdestCity() {
        return ResponseEntity.ok(statisticService.getColdestCity());
    }

    /**
     * Retrieves warmest city based on the available weather data.
     *
     * @return ResponseEntity containing the CityResponseDto of the warmest city
     */
    @GetMapping("/warmest")
    ResponseEntity<CityResponseDto> findWarmestCity() {
        return ResponseEntity.ok(statisticService.getWarmestCity());
    }


    /**
     * Retrieves maximum precipitation on the available weather data.
     *
     * @return ResponseEntity containing the CityResponseDto of the maximum precipitation city
     */
    @GetMapping("/maxPrecipitation")
    ResponseEntity<CityResponseDto> findCityWithMaxPrecipitation() {
        return ResponseEntity.ok(statisticService.getCityWithMaxPrecipitation());
    }
}
