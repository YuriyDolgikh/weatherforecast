package com.weatherforecast.config;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class AppConfig {
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String ADMIN_PASSWORD = "admin123";
    public static final String ADMIN_USERNAME = "admin";
    public final UserService userService;

    @Bean
    public CommandLineRunner lineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                try {
                    userService.getUserByEmailOrThrow(ADMIN_EMAIL);
                } catch (NotFoundException e) {
                    userService.registration(new UserRequestDto(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD));
                    User userAdmin = userService.getUserByEmailOrThrow(ADMIN_EMAIL);
                    userService.setConfirmedAdmin(userAdmin);
                }
            }
        };
    }
}
