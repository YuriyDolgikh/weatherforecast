package com.weatherforecast.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Forecast {

    Long id;
    City city;
    LocalDate forecastDate;
    String tempMax;
    String tempMin;
    String precipitation;
    LocalDateTime updateDateTime;
}
