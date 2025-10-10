package com.weatherforecast.repository;

import com.weatherforecast.entity.Forecast;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findByCityNameAndCreateTimeAfterOrderByForecastDateAsc(String cityName, LocalDateTime createTime);

    void deleteByCityName(String cityName);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE forecasts", nativeQuery = true)
    void truncateAndResetAutoIncrement();
}