package com.weatherforecast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.dto.forecast.TodayCityAverageWeatherResponseDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.repository.UserRepository;
import com.weatherforecast.service.ForecastService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class ForecastControllerGetTodayCitiesAverageWeatherTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ForecastRepository forecastRepository;

    @MockBean
    private ForecastService forecastService;

    @MockBean
    private CommandLineRunner lineRunner;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void dropDatabase() {
        userRepository.deleteAll();
        cityRepository.deleteAll();
        forecastRepository.deleteAll();
    }


    @BeforeEach
    void setUp() {

        User user1 = User.builder()
                .name("User1")
                .email("user1@company.com")
                .hashPassword("password1")
                .role(User.Role.USER)
                .status(User.Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .cities(new HashSet<>())
                .build();

        userRepository.save(user1);

    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "USER")
    void getTodayCitiesAverageWeatherIfUserLoggedIn() throws Exception {
        TodayCityAverageWeatherResponseDto berlin =
                TodayCityAverageWeatherResponseDto.builder()
                        .cityName("Berlin")
                        .avgTemp("15.0")
                        .precip("0.0")
                        .build();

        TodayCityAverageWeatherResponseDto london =
                TodayCityAverageWeatherResponseDto.builder()
                        .cityName("London")
                        .avgTemp("12.5")
                        .precip("1.2")
                        .build();

        when(forecastService.getTodayCitiesAverageWeather())
                .thenReturn(List.of(berlin, london));

        mockMvc.perform(get("/api/user/forecast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].cityName").value("Berlin"))
                .andExpect(jsonPath("$[0].avgTemp").value("15.0"))
                .andExpect(jsonPath("$[1].cityName").value("London"))
                .andExpect(jsonPath("$[1].avgTemp").value("12.5"));
    }

    @Test
    void getTodayCitiesAverageWeatherIfUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/user/forecast"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "USER")
    void getTodayCitiesAverageWeatherIfDataBaseEmpty() throws Exception {
        when(forecastService.getTodayCitiesAverageWeather())
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/user/forecast"))
                .andExpect(status().isNotFound());

    }


}