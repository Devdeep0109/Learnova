package com.example.Learnova.user.service;

import com.example.Learnova.exception.AuthenticationFailedException;
import com.example.Learnova.exception.BadRequestException;
import com.example.Learnova.exception.ResourceAlreadyExistsException;
import com.example.Learnova.user.config.jwt.JwtService;
import com.example.Learnova.user.dto.LoginRequestDto;
import com.example.Learnova.user.dto.RegisterRequestDto;
import com.example.Learnova.user.model.UserInfo;
import com.example.Learnova.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;


    public String register(RegisterRequestDto request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        UserInfo user = new UserInfo();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        userRepo.save(user);

        return "Register successfully!";
    }

    public String login(LoginRequestDto request) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            return jwtService.generateToken(
                    (org.springframework.security.core.userdetails.UserDetails)
                            Objects.requireNonNull(authentication.getPrincipal())
            );

        } catch (BadCredentialsException ex) {
            throw new AuthenticationFailedException("Invalid email or password");
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Authentication failed");
        }
    }
}