package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.UserRepository;
import com.weatherforecast.service.util.CityConverter;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class StatisticServiceGetAllFavoritesCitiesTest {
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

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cityRepository.deleteAll();
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
        City city = City.builder()
                .name("City1")
                .build();

        cityRepository.save(city);
    }

    @BeforeEach
    void getForecast() {
        ForecastRequestDto request = new ForecastRequestDto("Berlin");
        forecastService.get7DayForecast(request);
    }


    @Test
    void getAllCitiesInFavorites_whenNoUsers_throws() {
        userRepository.deleteAll();

        assertThrows(NotFoundException.class,
                () -> statisticService.getAllCitiesInFavorites());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void getAllCitiesInFavorites() {


        User checkCity = userService.getCurrentUser();

        cityService.addCityToFavorite("Berlin");

        Set<CityResponseDto> favorites = statisticService.getAllCitiesInFavorites();


        assertNotNull(favorites);
        assertTrue(favorites.stream().anyMatch(city -> city.getName().equals("Berlin")));


        assertTrue(checkCity.getCities().stream().anyMatch(c -> "Berlin".equals(c.getName())));


    }


}