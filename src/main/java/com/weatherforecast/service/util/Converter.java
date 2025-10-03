package com.weatherforecast.service.util;

import com.weatherforecast.dto.UserRequestDto;
import com.weatherforecast.dto.UserResponseDto;
import com.weatherforecast.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Converter {

    public User fromDto(UserRequestDto request){

        return User.builder()
                .name(request.getName())
                .name(request.getName())
                .email(request.getEmail())
                .hashPassword(request.getHashPassword())
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
