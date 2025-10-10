package com.weatherforecast.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
/**
 * The "cities" table stores basic information about cities.
 * Table-level configuration:
 * - {@code name = "cities"} → the database table will be named "cities".
 * - {@code uniqueConstraints = @UniqueConstraint(...)} → defines a UNIQUE constraint
 *   named "uk_city_name" on the column {@code name}.
 * Why is this important?
 * It guarantees that each city name can appear only once in the table.
 * In other words, duplicate city names are not allowed.
 * Example:
 *   name = "Berlin" → valid entry
 *   name = "Berlin" → second entry → rejected by DB, because the "uk_city_name" constraint is violated.
 */
@Table(
        name = "cities",
        uniqueConstraints = @UniqueConstraint(name = "uk_city_name", columnNames = "name"))
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "city name is required and must be not blank)")
    @Size(min = 3, max = 20)
    private String name;

}