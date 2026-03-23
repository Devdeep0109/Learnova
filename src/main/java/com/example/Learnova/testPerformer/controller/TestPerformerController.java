package com.example.Learnova.testPerformer.controller;

import com.example.Learnova.testPerformer.dto.*;
import com.example.Learnova.testPerformer.service.TestPerformerService;
import com.example.Learnova.user.config.customUserDetails.CustomUserDetails;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestPerformerController {

    private final TestPerformerService testPerformerService;

    // -------------------------------
    // JOIN TEST
    // -------------------------------
    @PreAuthorize("!hasRole('FACULTY')")
    @GetMapping("/join/{token}")
    public StartTestResponse startTest(
            @PathVariable String token,
            @AuthenticationPrincipal CustomUserDetails cud
    ) {
        UserInfo user = cud.getUser();
        return testPerformerService.startTest(token, user);
    }

    // -------------------------------
    // SUBMIT TEST
    // -------------------------------
    @PreAuthorize("!hasRole('FACULTY')")
    @PostMapping("/submit/{attemptId}")
    public SubmitTestResponse submitTest(
            @PathVariable Long attemptId,
            @RequestBody SubmitTestRequest request,
            @AuthenticationPrincipal CustomUserDetails cud
    ) {
        UserInfo user = cud.getUser();
        return testPerformerService.submitTest(attemptId, request, user);
    }
}