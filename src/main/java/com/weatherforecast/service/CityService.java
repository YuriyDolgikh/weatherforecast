package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.service.util.CityConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CityService implements CityServiceInterface {

    private final CityRepository cityRepository;
    private final CityConverter cityConverter;
    private final UserService userService;

    /**
     * Get all cities from database
     * @return List of CityResponseDto objects
     */
    @Override
    public List<CityResponseDto> getAllCities() {
        return cityConverter.toDtos(cityRepository.findAll());
    }

    /**
     * Get all cities from database with full details
     * @return List of City objects
     */
    public List<City> getAllCitiesFullDetails() {
        return cityRepository.findAll();
    }

    /**
     * Get city by name from the database
     * @param cityName
     * @return CityResponseDto object
     */
    @Override
    public CityResponseDto getCityByName(String cityName) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City with name = " + cityName + " not found in database"));
        return cityConverter.toDto(city);
    }

    @Override
    public List<CityResponseDto> getCitiesByNameContainsIgnoreCase(String cityName) {
        List<City> cities = cityRepository.findByNameContainingIgnoreCase(cityName);
        return cityConverter.toDtos(cities);
    }

    public List<CityResponseDto> getCitiesByCurrentUser() {
        User user = userService.getCurrentUser();
        return cityConverter.toDtos(user.getCities());
    }

    @Transactional
    @Override
    public List<CityResponseDto> addCityToFavorite(String cityName) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City with name = " + cityName
                        + " is not found in database. At first get forecast for this city"));
        User user = userService.getCurrentUser();
        user.getCities().add(city);
        userService.saveUser(user);
        return cityConverter.toDtos(user.getCities());
    }

    @Transactional
    @Override
    public List<CityResponseDto> deleteCityFromFavorite(String cityName) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City with name = " + cityName + " is not found in database"));
        User user = userService.getCurrentUser();
        Set<City> userFavoriteCities = user.getCities();
        if (!userFavoriteCities.contains(city)) {
            throw new NotFoundException("City with name = " + cityName + " is not found in user favorite cities");
        }
        user.getCities().remove(city);
        userService.saveUser(user);
        return cityConverter.toDtos(user.getCities());
    }

    @Override
    public void saveCity(String cityName) {
        Optional<City> city = cityRepository.findByName(cityName);
        if (city.isEmpty()) {
            cityRepository.save(new City(null, cityName));
        }
    }
}
