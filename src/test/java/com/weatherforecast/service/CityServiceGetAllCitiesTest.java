package com.weatherforecast.service;

import com.weatherforecast.dto.city.CityResponseDto;
import com.weatherforecast.entity.City;
import com.weatherforecast.entity.User;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class CityServiceGetAllCitiesTest {

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

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
        cityRepository.deleteAll();
        userRepository.deleteAll();
        testUser = User.builder()
                .name("user1")
                .email("user1@company.com")
                .hashPassword("pass123")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .cities(new HashSet<>())
                .build();

        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    void testGetAllCities() {
        cityRepository.saveAll(List.of(
                new City(null, "Berlin"),
                new City(null, "Paris")
        ));
        List<CityResponseDto> cities = cityService.getAllCities();
        assertEquals(2, cities.size());
    }

    @Test
    void testGetAllCitiesIfEmptyOrNull() {
        List<CityResponseDto> cities = cityService.getAllCities();
        assertEquals(0, cities.size());
    }





}