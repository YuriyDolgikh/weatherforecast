package com.weatherforecast.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
/**
 * The "forecasts" table stores daily weather forecasts for cities.
 * Table-level configuration:
 * - {@code name = "forecasts"} → the table name in the database.
 * - {@code uniqueConstraints} → defines a UNIQUE constraint named "uk_city_date"
 *   on the combination of {@code city_id} and {@code forecast_date}.
 * Why is this important?
 * It guarantees that there can be only ONE forecast entry per city for a given date.
 * In other words: you cannot insert two forecasts for the same city and the same date.
 * Example:
 *   city_id=1, forecast_date=2025-10-03 → valid entry
 *   city_id=1, forecast_date=2025-10-03 → second entry → rejected by DB
 */
@Table(
        name = "forecasts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_city_date",
                columnNames = {"city_id", "forecast_date"}
        )
)
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "City name is required")
    @NotBlank(message = "City name cannot be blank")
    @Column(name = "city_name")
    private String cityName;

    @NotNull(message = "Forecast date is required")
    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    @NotBlank(message = "Maximum temperature is required")
    @Column(name = "max_temp", nullable = false, length = 20)
    private String maxTemp;

    @NotBlank(message = "Minimum temperature is required")
    @Column(name = "min_temp", nullable = false, length = 20)
    private String minTemp;

    @NotBlank(message = "Precipitation is required")
    @Column(name = "precip", nullable = false, length = 20)
    private String precip;

    @NotNull(message = "Create time is required")
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

}
