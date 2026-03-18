package com.example.Learnova.testCreator.controller;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.service.TestGeneratorService;
import com.example.Learnova.user.config.customUserDetails.CustomUserDetails;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestGeneratorController {

    private final TestGeneratorService testGeneratorService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('FACULTY') or hasRole('ADMIN') or hasRole('SELF_USER')")
    public ResponseEntity<?> generate(
            @RequestBody GenerateTestRequest req,
            Authentication authentication) throws Exception {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserInfo user = userDetails.getUser();

        Tests test = testGeneratorService.generateTest(req, user);

        return ResponseEntity.ok(test);
    }
}
