package com.example.Learnova.user.dto;

import com.example.Learnova.user.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String email;
    private Role role;
}