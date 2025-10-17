package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.BadRequestException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class StatisticServiceGetColdestCityTest {
    @MockBean
    private CommandLineRunner lineRunner;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private ForecastRepository forecastRepository;

    @AfterEach
    void tearDown() {
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

        userRepository.save(user1);

        Forecast forecast1 = Forecast.builder()
                .cityName("Berlin")
                .createTime(LocalDateTime.now())
                .minTemp("10")
                .maxTemp("20")
                .forecastDate(LocalDate.now())
                .precip("2")
                .build();

        Forecast forecast2 = Forecast.builder()
                .cityName("London")
                .createTime(LocalDateTime.now())
                .minTemp("1")
                .maxTemp("3")
                .forecastDate(LocalDate.now())
                .precip("4")
                .build();

        forecastRepository.save(forecast1);
        forecastRepository.save(forecast2);

        City city1 = City.builder()
                .name("London")
                .build();

        City city2 = City.builder()
                .name("Berlin")
                .build();

        cityRepository.save(city1);
        cityRepository.save(city2);
    }


    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void getColdestCity() {
        User checkCity = userService.getCurrentUser();

        CityResponseDto city = statisticService.getColdestCity();

        assertNotNull(city);
        assertEquals("Berlin", city.getName());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void coldestCityNotFoundIfDatabaseIsEmpty() {

        User user = userService.getCurrentUser();

        forecastRepository.deleteAll();

        assertThrows(BadRequestException.class,
                () -> statisticService.getColdestCity());
    }
}