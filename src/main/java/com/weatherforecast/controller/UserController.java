package com.weatherforecast.controller;


import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.dto.user.UserUpdateRequestDto;
import com.weatherforecast.entity.User;
import com.weatherforecast.service.UserService;
import com.weatherforecast.service.util.UserConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    /**
     * Retrieves information about the currently authenticated user.
     * @return a ResponseEntity containing the UserResponseDto of the current user
     */
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserInfo() {
        return ResponseEntity.ok(userConverter.toDto(userService.getCurrentUser()));
    }

    /**
     * Delete themselves from the system
     *
     * @param - no parameters
     * @return a ResponseEntity containing the UserResponseDto of the deleted user
     */
    @DeleteMapping
    public ResponseEntity<UserResponseDto> deleteUser() {
        User currentUser = userService.getCurrentUser();
        userService.deleteUser(currentUser.getId());
        return ResponseEntity.ok(userConverter.toDto(currentUser));
    }

    /**
     * Updates the currently authenticated user's information based on the provided data.
     * @param requestDto the UserUpdateRequestDto containing updated user details
     * @return a ResponseEntity containing the updated UserResponseDto
     */
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateRequestDto requestDto) {
        return ResponseEntity.ok(userService.updateUser(requestDto));
    }
}

