package com.weatherforecast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.dto.forecast.DailyForecastResponseDto;
import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.forecast.WeeklyForecastResponseDto;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.entity.User;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class ForecastControllerGetWeatherForecastTest {
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
                .role(User.Role.ADMIN)
                .status(User.Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .cities(new HashSet<>())
                .build();

        User user2 = User.builder()
                .name("User2")
                .email("user2@company.com")
                .hashPassword("password2")
                .role(User.Role.USER)
                .status(User.Status.CONFIRMED)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .cities(new HashSet<>())
                .build();


        userRepository.save(user1);
        userRepository.save(user2);


    }

    @Test
    @WithMockUser(username = "user2@company.com", roles = "USER")
    void getWeatherForecast() throws Exception {
        ForecastRequestDto requestBerlin = ForecastRequestDto.builder()
                .name("Berlin")
                .build();

        List<Forecast> forecastsFromApi = createForecastList(7);
        List<DailyForecastResponseDto> forecastsFromApiDtos = createForecastListDtos(forecastsFromApi);
        WeeklyForecastResponseDto expectedResponse = new WeeklyForecastResponseDto(requestBerlin.getName(), forecastsFromApiDtos);

        when(forecastService.get7DayForecast(any(ForecastRequestDto.class))).thenReturn(expectedResponse);


        mockMvc.perform(post("/api/user/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBerlin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("Berlin"))
                .andExpect(jsonPath("$.forecasts.length()").value(7));
    }

    private List<Forecast> createForecastList(int count) {
        List<Forecast> forecasts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Forecast forecast = new Forecast();

            forecast.setId((long) (i + 1));
            forecast.setCityName("Berlin");
            forecast.setForecastDate(LocalDate.now().plusDays(i));
            forecast.setMaxTemp(String.valueOf(25.0 + i));
            forecast.setMinTemp(String.valueOf(15.0 + i));
            forecast.setPrecip(String.valueOf(0.0 + i));
            forecast.setCreateTime(LocalDateTime.now());

            forecasts.add(forecast);
        }
        return forecasts;
    }

    private List<DailyForecastResponseDto> createForecastListDtos(List<Forecast> forecastsFromDb) {
        List<DailyForecastResponseDto> forecastsDtos = new ArrayList<>();
        for (Forecast forecast : forecastsFromDb) {
            DailyForecastResponseDto forecastDto = new DailyForecastResponseDto();
            forecastDto.setDate(forecast.getForecastDate());
            forecastDto.setMaxTemp(forecast.getMaxTemp());
            forecastDto.setMinTemp(forecast.getMinTemp());
            forecastDto.setPrecip(forecast.getPrecip());
            forecastsDtos.add(forecastDto);
        }
        return forecastsDtos;
    }
}