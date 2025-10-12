package com.weatherforecast.controller;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

   private final StatisticService statisticService;

   @GetMapping("/findAll")
   ResponseEntity<List<UserResponseDto>> findAll(){
       return ResponseEntity.ok(statisticService.getAllUsers());
   }

   @GetMapping("/allFavoritesCities")
    ResponseEntity<Set<CityResponseDto>> findAllFavoritesCitiesByAllUsers(){
       return ResponseEntity.ok(statisticService.getAllCitiesInFavorites());
   }

   @GetMapping("/allFavoritesCitiesByUser/{id}")
    ResponseEntity<Set<CityResponseDto>> findAllFavoritesCitiesByUserId(@PathVariable Long id){
       return ResponseEntity.ok(statisticService.getAllCitiesInFavoriteByUserId(id));
   }

   @GetMapping("/coldest")
    ResponseEntity<CityResponseDto> findColdestCity(){
       return ResponseEntity.ok(statisticService.getColdestCity());
   }

    @GetMapping("/warmest")
    ResponseEntity<CityResponseDto> findWarmestCity(){
        return ResponseEntity.ok(statisticService.getWarmestCity());
    }

    @GetMapping("/maxPrecipitation")
    ResponseEntity<CityResponseDto> findCityWithMaxPrecipitation(){
        return ResponseEntity.ok(statisticService.getCityWithMaxPrecipitation());
    }
}
