package com.weatherforecast.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Forecast {

    private Long id;
    private City city;
    private LocalDate forecastDate;
    private String tempMax;
    private String tempMin;
    private String precipitation;
    private LocalDateTime updateDateTime;
}
