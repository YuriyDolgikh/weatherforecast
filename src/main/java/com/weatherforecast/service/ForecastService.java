package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.TodayCityAverageWeatherResponseDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.service.util.ForecastConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService implements ForecastServiceInterface{

    private final ForecastRepository repository;
    private final CityService cityService;
    private final OutWeatherApi outWeatherApi;
    private final ForecastConverter converter;

    private static final Integer EXPIRATION_PERIOD_MINUTES = 240;

    @Transactional
    @Override
    public WeeklyForecastResponseDto get7DayForecast(ForecastRequestDto request) {
        List<Forecast> forecastsFromDb = repository.findByCityNameAndCreateTimeAfterOrderByForecastDateAsc(
                request.getName(),
                LocalDateTime.now().minusMinutes(EXPIRATION_PERIOD_MINUTES)
        );
        if (!forecastsFromDb.isEmpty() && forecastsFromDb.size() >= 7) {
            return converter.toDto(request.getName(), forecastsFromDb);    // Return data from DB
        }
        List<Forecast> forecastsFromApi = outWeatherApi.receive7DayForecast(request.getName());  // Get new data from API
        repository.deleteByCityName(request.getName());     // Delete old records for the city
        repository.saveAll(forecastsFromApi);
        cityService.saveCity(request.getName()); // Save the city to DB if it doesn't present in DB
        return converter.toDto(request.getName(), forecastsFromApi);
    }

    @Scheduled(cron = "0 0 */4 * * *")
    @Transactional
    @Override
    public void updateForecastForAllCitiesFromDatabase() {
        List<City> citiesInDatabase = cityService.getAllCitiesFullDetails();
        for (City city : citiesInDatabase) {
            get7DayForecast(new ForecastRequestDto(city.getName()));
        }

    }

    @Transactional
    @Override
    public List<TodayCityAverageWeatherResponseDto> getTodayCitiesAverageWeather() {
        List<CityResponseDto> favouriteCities = cityService.getCitiesByCurrentUser();
        List<TodayCityAverageWeatherResponseDto> todayWeathers = new ArrayList<>();
        for (CityResponseDto city : favouriteCities) {
            Forecast todayCityForecast = repository.findByCityNameAndForecastDate(city.getName(), LocalDate.now())
                    .orElseThrow(() -> new NotFoundException("Today weather for city: " + city.getName() + " not found"));
            todayWeathers.add(converter.toDto(city.getName(), todayCityForecast));
        }
        return todayWeathers;
    }

}