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
    /**
     * Many-to-one relationship to the {@link City} entity.
     * - {@code @ManyToOne(optional = false)} → each forecast must be linked to one city
     * (the relation cannot be null).
     * - {@code @JoinColumn(name = "city_id", nullable = false)} → defines the foreign key
     * column in the "forecasts" table that references the "cities" table.
     * - {@code foreignKey = @ForeignKey(name = "fk_forecast_city")} → explicit name for the
     * foreign key constraint in the database, which improves readability and makes
     * debugging/migrations easier.
     * In practice: every forecast row is tied to a single city, and the database ensures
     * referential integrity between "forecasts.city_id" and "cities.id".
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_forecast_city"))
    private City city;

    @NotNull(message = "Forecast date is required")
    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    @NotBlank(message = "Maximum temperature is required")
    @Column(name = "temp_max", nullable = false, length = 20)
    private String tempMax;

    @NotBlank(message = "Minimum temperature is required")
    @Column(name = "temp_min", nullable = false, length = 20)
    private String tempMin;

    @NotBlank(message = "Precipitation is required")
    @Column(name = "precipitation", nullable = false, length = 20)
    private String precipitation;

    @NotNull(message = "Update date is required")
    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDateTime;

}