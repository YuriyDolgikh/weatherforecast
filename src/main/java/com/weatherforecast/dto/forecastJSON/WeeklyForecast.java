package com.weatherforecast.dto.forecastJSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyForecast {
    @JsonProperty(value = "city_name", required = true)
    private String cityName;
    @JsonProperty(value = "data", required = true)
    private List<DailyForecast> data;
}