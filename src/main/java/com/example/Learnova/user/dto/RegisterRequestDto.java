package com.example.Learnova.user.dto;

import com.example.Learnova.user.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    private String email;
    private String password;
    private Role role;
}
