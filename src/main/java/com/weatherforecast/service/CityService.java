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
     *
     * @return List of CityResponseDto objects
     */
    @Override
    public List<CityResponseDto> getAllCities() {
        return cityConverter.toDtos(cityRepository.findAll());
    }

    /**
     * Get all cities from database with full details
     *
     * @return List of City objects
     */
    public List<City> getAllCitiesFullDetails() {
        return cityRepository.findAll();
    }

    /**
     * Retrieves a city from the database by its name.
     * If a city with the given name does not exist, a {@link NotFoundException}
     * will be thrown.
     *
     * @param cityName the name of the city to retrieve (case-sensitive)
     * @return a {@link CityResponseDto} containing information about the found city
     * @throws NotFoundException if the city with the specified name is not found in the database
     */
    @Override
    public CityResponseDto getCityByName(String cityName) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City with name = " + cityName + " not found in database"));
        return cityConverter.toDto(city);
    }


    @Override
    public List<CityResponseDto> getCitiesByNameContainsIgnoreCase(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            throw new IllegalArgumentException("City name must be provided");
        }
        List<City> cities = cityRepository.findByNameContainingIgnoreCase(cityName);
        return cityConverter.toDtos(cities);
    }

    /**
     * Retrieves all cities currently added to the favorites of the logged-in user.
     *
     * @return a list of {@link CityResponseDto} objects representing the user's favorite cities
     */
    public List<CityResponseDto> getCitiesByCurrentUser() {
        User user = userService.getCurrentUser();
        return cityConverter.toDtos(user.getCities());
    }

    /**
     * Adds the specified city to the list of favorite cities of the currently logged-in user.
     *
     * @param cityName the name of the city to add to favorites
     * @return an updated list of {@link CityResponseDto} representing the user's favorite cities
     * @throws NotFoundException if the city with the specified name is not found in the database
     */
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

    /**
     * Removes the specified city from the list of favorite cities of the currently logged-in user.
     *
     * @param cityName the name of the city to remove from favorites
     * @return an updated list of {@link CityResponseDto} representing the user's favorite cities
     * @throws NotFoundException if the city with the specified name is not found in the database
     */
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

    /**
     * Saves a new city in the database if it does not already exist.
     *
     * @param cityName the name of the city to save
     */
    @Override
    public void saveCity(String cityName) {
        Optional<City> city = cityRepository.findByName(cityName);
        if (city.isEmpty()) {
            cityRepository.save(new City(null, cityName));
        }
    }
}
