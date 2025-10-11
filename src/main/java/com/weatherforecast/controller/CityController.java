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

    @GetMapping("/cities/all")
    public ResponseEntity<List<CityResponseDto>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponseDto>> getMyFavouriteCities() {
        return ResponseEntity.ok(cityService.getCitiesByCurrentUser());
    }

    @GetMapping("/cities/add")
    public ResponseEntity<List<CityResponseDto>> addCityToFavorite(@RequestParam String cityName) {
        return ResponseEntity.ok(cityService.addCityToFavorite(cityName));
    }

    @GetMapping("/cities/delete")
    public ResponseEntity<List<CityResponseDto>> deleteCityFromFavorite(@RequestParam String cityName) {
        return ResponseEntity.ok(cityService.deleteCityFromFavorite(cityName));
    }

}

