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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class StatisticServiceGetAllFavoritesCitiesByUserIdTest {
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
                .minTemp("1")
                .maxTemp("2")
                .forecastDate(LocalDate.now())
                .precip("2")
                .build();


        forecastRepository.save(forecast1);


        City city1 = City.builder()
                .name("Berlin")
                .build();


        cityRepository.save(city1);


    }




    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void getAllCitiesInFavorites_whenNoUsers_throws() {

        User user = userService.getCurrentUser();


        assertThrows(BadRequestException.class,
                () -> statisticService.getAllCitiesInFavoriteByUserId(user.getId()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void getAllCitiesInFavoriteByUserId() {

        User checkCity = userService.getCurrentUser();

        cityService.addCityToFavorite("Berlin");

        Set<CityResponseDto> favoritesCities = statisticService.getAllCitiesInFavoriteByUserId(checkCity.getId());

        assertNotNull(favoritesCities);
        assertTrue(favoritesCities.stream().anyMatch(city -> city.getName().equals("Berlin")));


        assertTrue(checkCity.getCities().stream().anyMatch(c -> "Berlin".equals(c.getName())));


    }
}