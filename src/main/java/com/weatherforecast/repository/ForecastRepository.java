package com.weatherforecast.repository;

import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    Optional<Forecast> findByCityAndForecastDate(City city, LocalDate date);

}