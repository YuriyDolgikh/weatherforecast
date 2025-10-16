package com.weatherforecast.controller;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final UserService userService;

    /**
     * Registers a new user based on the provided user details.
     *
     * @param userRequestDto the user data for registration
     * @return a ResponseEntity containing the created UserResponseDto
     * *         with HTTP status 201 (Created)
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registration(userRequestDto));
    }


    /**
     * Confirms a user's email based on the provided confirmation code.
     *
     * @param code the unique confirmation code sent to the user's email
     * @return a ResponseEntity containing a success message if the confirmation is successful
     */
    @GetMapping("/confirmation")
    public ResponseEntity<String> confirmation(@Valid @RequestParam String code) {
        return ResponseEntity.ok(userService.confirmationEmail(code));
    }

}
