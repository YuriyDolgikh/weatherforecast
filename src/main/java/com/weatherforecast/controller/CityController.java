package com.weatherforecast.controller;


import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class CityController {

    private final CityService cityService;

    /**
     * Retrieves all cities available in the system.
     *
     * @return ResponseEntity containing a list of CityResponseDto objects
     */
    @GetMapping("/cities/all")
    public ResponseEntity<List<CityResponseDto>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    /**
     * Retrieves the current user's favorite cities that are available in the system.
     *
     * @return ResponseEntity containing a list of CityResponseDto objects
     */
    @GetMapping("/cities")
    public ResponseEntity<List<CityResponseDto>> getMyFavouriteCities() {
        return ResponseEntity.ok(cityService.getCitiesByCurrentUser());
    }

    /**
     * Adds a city to the current user's list of favorite cities.
     *
     * @param cityName the name of the city to be added to favorites
     * @return a ResponseEntity containing the updated list of the user's favorite cities as CityResponseDto objects
     */
    @GetMapping("/cities/add")
    public ResponseEntity<List<CityResponseDto>> addCityToFavorite(@RequestParam String cityName) {
        return ResponseEntity.ok(cityService.addCityToFavorite(cityName));
    }

    /**
     * Removes a city from the current user's list of favorite cities.
     *
     * @param cityName the name of the city to remove from favorites
     * @return a ResponseEntity containing the updated list of the user's favorite cities as CityResponseDto objects
     */
    @GetMapping("/cities/delete")
    public ResponseEntity<List<CityResponseDto>> deleteCityFromFavorite(@RequestParam String cityName) {
        return ResponseEntity.ok(cityService.deleteCityFromFavorite(cityName));
    }
}

