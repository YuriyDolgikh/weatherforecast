package com.weatherforecast.controller;

import com.weatherforecast.entity.City;
import com.weatherforecast.entity.Forecast;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.ForecastRepository;
import com.weatherforecast.repository.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class AdminControllerGetWarmestCityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ForecastRepository forecastRepository;

    @MockBean
    private CommandLineRunner lineRunner;


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

    @AfterEach
    void dropDatabase() {
        userRepository.deleteAll();
        cityRepository.deleteAll();
        forecastRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void findWarmestCityIfAdminAndIfDatabaseNotEmpty() throws Exception {
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
        mockMvc.perform(get("/api/admin/warmest"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "user2@company.com", roles = "USER")
    void findWarmestCityIfUser() throws Exception {
        mockMvc.perform(get("/api/admin/warmest"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "ADMIN")
    void findWarmestCityIfAdminAndIfDataBaseIsEmpty() throws Exception {
        mockMvc.perform(get("/api/admin/warmest"))
                .andExpect(status().isBadRequest());

    }
}