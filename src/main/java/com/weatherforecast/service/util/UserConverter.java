package com.weatherforecast.service.util;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User fromDto(UserRequestDto request){

        String encodedPassword = passwordEncoder.encode(request.getHashPassword());

        return User.builder()
                .name(request.getName())
                .name(request.getName())
                .email(request.getEmail())
                .hashPassword(encodedPassword)
                .build();
    }

    public UserResponseDto toDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public List<UserResponseDto> fromUsers(List<User> users){
        return users.stream()
                .map(user -> toDto(user))
                .toList();
    }
}
