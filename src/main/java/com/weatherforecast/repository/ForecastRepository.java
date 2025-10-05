package com.weatherforecast.repository;

import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByCityAndForecastDate(City city, LocalDateTime date);
    List<Forecast> findByCityAndForecastDateBetweenOrderByForecastDate(City city, LocalDateTime startDate, LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE forecasts", nativeQuery = true)
    void truncateAndResetAutoIncrement();
}