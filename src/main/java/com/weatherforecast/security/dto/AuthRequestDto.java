package com.weatherforecast.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDto {

    private String username;
    private String password;

}
