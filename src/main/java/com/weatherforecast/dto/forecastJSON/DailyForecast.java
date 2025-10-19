package com.weatherforecast.dto.forecastJSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyForecast {

    @JsonProperty(value = "datetime", required = true)
    private String dateTime;

    @JsonProperty(value = "max_temp", required = true)
    private Double maxTemp;

    @JsonProperty(value = "min_temp", required = true)
    private Double minTemp;

    @JsonProperty(value = "precip", required = true)
    private Double precipitation;

}