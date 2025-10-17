package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class CityServiceGetCityByNameTest {

    @MockBean
    private CommandLineRunner lineRunner;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityService cityService;

    private User testUser;

    @BeforeEach
    void setUp() {
        City berlin = new City();
        berlin.setName("Berlin");

        City paris = new City();
        paris.setName("Paris");
        cityRepository.saveAll(List.of(berlin, paris));
        Set<City> cities = new HashSet<>();
        cities.add(berlin);

        testUser = User.builder()
                .name("user1")
                .email("user1@company.com")
                .hashPassword("pass123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .cities(cities)
                .build();

        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    void testGetCityByNameExists() {
        CityResponseDto city = cityService.getCityByName("Berlin");
        assertNotNull(city);
        assertEquals("Berlin", city.getName());
    }

    @Test
    void testGetCityByNameNotExists() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cityService.getCityByName("Bern")
        );
        assertEquals("City with name = Bern not found in database", exception.getMessage());
    }

    @Test
    void testGetCityByNameIfNotFound() {
        assertThrows(NotFoundException.class, () -> cityService.getCityByName("London"));
    }

    @Test
    void testGetCityByNameIsNull() {
        assertThrows(NotFoundException.class, () -> cityService.getCityByName(null));
    }

    @Test
    void testGetCityByNameIsEmpty() {
        assertThrows(NotFoundException.class, () -> cityService.getCityByName(""));
    }
}