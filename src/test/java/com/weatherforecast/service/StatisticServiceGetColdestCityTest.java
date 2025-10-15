package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
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
    void createUser() {
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

    }

    @BeforeEach
    void createCity() {
        ForecastRequestDto request1 = new ForecastRequestDto("Berlin");
        forecastService.get7DayForecast(request1);
        ForecastRequestDto request2 = new ForecastRequestDto("Tomsk");
        forecastService.get7DayForecast(request2);
    }


    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void getColdestCity() {
        User checkCity = userService.getCurrentUser();


        CityResponseDto city = statisticService.getColdestCity();


        assertNotNull(city);
        assertTrue(city.getName().equals("Tomsk"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void coldestCityNotFoundIfDatabaseIsEmpty() {

        User user = userService.getCurrentUser();

        forecastRepository.deleteAll();




        assertThrows(NotFoundException.class,
                () -> statisticService.getColdestCity());
    }
}