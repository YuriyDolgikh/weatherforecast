package com.weatherforecast.controller;

import com.weatherforecast.security.dto.AuthRequestDto;
import com.weatherforecast.security.dto.AuthResponseDto;
import com.weatherforecast.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user based on the provided credentials and generates a JWT token.
     *
     * @param authRequestDto the authentication request containing the user's credentials
     * @return ResponseEntity containing an AuthResponseDto with the generated JWT token
     */
    @PostMapping
    ResponseEntity<AuthResponseDto> authenticate(@Valid @RequestBody AuthRequestDto authRequestDto) {
        String jwt = authService.generateJwt(authRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(jwt));
    }
}
