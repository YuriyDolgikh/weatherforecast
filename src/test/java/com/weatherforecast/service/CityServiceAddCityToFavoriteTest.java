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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
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
class CityServiceAddCityToFavoriteTest {

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
    @WithMockUser(username = "user1@company.com", roles = "USER")
    void testAddCityToFavorite() {
        List<CityResponseDto> result = cityService.addCityToFavorite("Paris");
        assertEquals(2, result.size());
    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "USER")
    void testAddCityToFavorite_NotFound() {
        assertThrows(NotFoundException.class, () -> cityService.addCityToFavorite("Rome"));
    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "USER")
    void testAddCityToFavoriteIfCityNull() {
        assertThrows(NotFoundException.class, () -> cityService.addCityToFavorite(null));
    }

    @Test
    @WithMockUser(username = "user1@company.com", roles = "USER")
    void testAddCityToFavoriteIfCityEmpty() {
        assertThrows(NotFoundException.class, () -> cityService.addCityToFavorite(""));
    }

}