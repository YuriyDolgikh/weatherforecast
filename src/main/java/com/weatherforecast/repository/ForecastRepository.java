package com.weatherforecast.repository;

import com.weatherforecast.entity.Forecast;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findByCityNameAndCreateTimeAfterOrderByForecastDateAsc(String cityName, LocalDateTime createTime);

    List<Forecast> findByForecastDate(LocalDate forecastDate);

    Optional<Forecast> findByCityNameAndForecastDate(String cityName, LocalDate forecastDate);

    void deleteByCityName(String cityName);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE forecasts", nativeQuery = true)
    void truncateAndResetAutoIncrement();
}