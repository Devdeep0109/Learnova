package com.example.Learnova.testCreator.controller;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.dto.QuestionDTO;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.model.enums.Status;
import com.example.Learnova.testCreator.repository.TestsRepository;
import com.example.Learnova.testCreator.service.QuestionService;
import com.example.Learnova.testCreator.service.TestGeneratorService;
import com.example.Learnova.user.config.customUserDetails.CustomUserDetails;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestGeneratorController {

    private final TestGeneratorService testGeneratorService;
    private final QuestionService questionService;
    private final TestsRepository testsRepository;

    @GetMapping("/{testId}/status")
    public Status getStatus(@PathVariable Long testId) {

        Tests test = testsRepository.findById(testId)
                .orElseThrow();

        return test.getStatus();
    }

    @GetMapping("/{testId}/questions")
    public List<QuestionDTO> getTestQuestions(@PathVariable Long testId) {
        return questionService.getQuestionsByTest(testId);
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(
            @RequestBody GenerateTestRequest req,
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Tests test =
                testGeneratorService.createTest(req, userDetails.getUser());

        return ResponseEntity.ok(
                Map.of(
                        "testId", test.getId(),
                        "status", test.getStatus()
                )
        );
    }
}
