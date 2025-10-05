package com.weatherforecast.repository;

import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    Optional<Forecast> findByCityAndForecastDate(City city, LocalDateTime date);
    List<Forecast> findByCityAndForecastDateBetweenOrderByForecastDate(City city, LocalDateTime startDate, LocalDateTime endDate);

}