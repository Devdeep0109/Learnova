package com.example.Learnova.testPerformer.controller;

import com.example.Learnova.testPerformer.dto.*;
import com.example.Learnova.testPerformer.service.TestAttemptAnswerReview;
import com.example.Learnova.testPerformer.service.TestPerformerService;
import com.example.Learnova.user.config.customUserDetails.CustomUserDetails;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestPerformerController {

    private final TestPerformerService testPerformerService;
    private final TestAttemptAnswerReview testAttemptAnswerReview;

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

    //------------------------
    //MY RESULT
//  --------------------------
    @PreAuthorize("!hasRole('FACULTY')")
    @GetMapping("/my-results")
    public List<MyResultResponse> getMyResults(
            @AuthenticationPrincipal CustomUserDetails cud
    ) {
        return testPerformerService.getMyResults(cud.getUser());
    }

    // -------------------------------
    // ATTEMPT FULL REVIEW
    // -------------------------------
    @PreAuthorize("!hasRole('FACULTY')")
    @GetMapping("/attempts/{attemptId}/review")
    public AttemptReviewResponse getAttemptReview(
            @PathVariable Long attemptId,
            @AuthenticationPrincipal CustomUserDetails cud
    ) {
        return testAttemptAnswerReview.getAttemptReview(
                attemptId,
                cud.getUser()
        );
    }
}