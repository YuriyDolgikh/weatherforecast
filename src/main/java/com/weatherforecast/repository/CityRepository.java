package com.weatherforecast.repository;

import com.weatherforecast.entity.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByNameContainingIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE cities", nativeQuery = true)
    void truncateAndResetAutoIncrement();
}