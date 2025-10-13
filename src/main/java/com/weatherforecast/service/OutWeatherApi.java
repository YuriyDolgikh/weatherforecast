package com.weatherforecast.service;

import com.weatherforecast.dto.forecastJSON.DailyForecast;
import com.weatherforecast.dto.forecastJSON.WeeklyForecast;
import com.weatherforecast.entity.Forecast;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutWeatherApi {

    @Value("${weather.api-key}")
    private String API_KEY = "1b7700a0a1da4b37bd47798363d96942" ;
    private static final String REQUEST_URL = "https://api.weatherbit.io/v2.0/forecast/daily";

    private final RestTemplate restTemplate;

    public List<Forecast> receive7DayForecast(String cityName) {
        try {
            String clientRequest = createUrl(cityName);
            URI uri = new URI(clientRequest);

            System.out.println("Sending request to: " + clientRequest);

            ResponseEntity<WeeklyForecast> response =
                    restTemplate.getForEntity(uri, WeeklyForecast.class);

            System.out.println("Received response status: " + response.getStatusCode());

            if (response.getBody() != null && response.getBody().getData() != null
                    && !response.getBody().getData().isEmpty()) {

                WeeklyForecast apiResponse = response.getBody();
                return convertToEntities(cityName, apiResponse.getData());

            } else {
                throw new RuntimeException("No weather data found in response for city: " + cityName);
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for weather API request", e);
        } catch (RestClientException e) {
            throw new RuntimeException("Error calling weather API", e);
        }
    }

    private List<Forecast> convertToEntities(String cityName, List<DailyForecast> dailyForecasts) {
        List<Forecast> entities = new ArrayList<>();

        for (DailyForecast forecast : dailyForecasts) {
            Forecast entity = new Forecast(
                    null,
                    cityName,
                    LocalDate.parse(forecast.getDateTime()),
                    String.valueOf(forecast.getMaxTemp()),
                    String.valueOf(forecast.getMinTemp()),
                    String.valueOf(forecast.getPrecipitation()),
                    LocalDateTime.now()
            );
            entities.add(entity);
        }

        return entities;
    }

    private String createUrl(String cityName) {
        return UriComponentsBuilder.fromUriString(REQUEST_URL)
                .queryParam("city", cityName)
                .queryParam("key", API_KEY)
                .queryParam("days", 7)
                .build()
                .toUriString();
    }
}