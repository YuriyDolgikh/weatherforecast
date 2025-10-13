package com.weatherforecast.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather Forecast API")
                        .description("API for getting weather forecast")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Group 05052-m-be")
                                .email("project05052025@gmail.com")));
    }
}