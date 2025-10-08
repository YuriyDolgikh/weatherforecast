package com.weatherforecast.controller;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registration(userRequestDto));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String code) {
        return ResponseEntity.ok(userService.confirmationEmail(code));
    }

}
