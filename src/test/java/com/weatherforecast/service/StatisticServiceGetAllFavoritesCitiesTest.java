package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.dto.forecast.ForecastRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.ForecastRepository;
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

import java.time.LocalDate;
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

    @Autowired
    private ForecastRepository forecastRepository;

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

        Forecast forecast1 = Forecast.builder()
                .cityName("Berlin")
                .createTime(LocalDateTime.now())
                .minTemp("1")
                .maxTemp("2")
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
    void getAllCitiesInFavoritesIfDataBaseIsEmpty() {
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
        cityService.addCityToFavorite("London");

        Set<CityResponseDto> favorites = statisticService.getAllCitiesInFavorites();


        assertNotNull(favorites);
        assertTrue(favorites.stream().anyMatch(city -> city.getName().equals("Berlin")));
        assertTrue(favorites.stream().anyMatch(city -> city.getName().equals("London")));


        assertTrue(checkCity.getCities().stream().anyMatch(c -> "Berlin".equals(c.getName())));
        assertTrue(checkCity.getCities().stream().anyMatch(c -> "London".equals(c.getName())));


    }


}