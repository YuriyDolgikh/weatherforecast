package com.weatherforecast.config;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.service.CodeConfirmationService;
import com.weatherforecast.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class AppConfig {
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String ADMIN_PASSWORD = "admin123";
    public static final String ADMIN_USERNAME = "admin";
    public final UserService userService;
    public final CodeConfirmationService codeConfirmationService;

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
                    List<ConfirmationCode> codes = codeConfirmationService.findCodesByUser(userAdmin);
                    for (ConfirmationCode code : codes) {
                        codeConfirmationService.changeConfirmationStatusByCode(code.getCode());
                    }
                }
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
