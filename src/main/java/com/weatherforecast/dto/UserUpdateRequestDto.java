package com.weatherforecast.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {

    private String email;

    @NotBlank(message = "Name is required and must be not blank)")
    @Size(min = 3, max = 25)
    private String lastName;

    private String hashPassword;

}
