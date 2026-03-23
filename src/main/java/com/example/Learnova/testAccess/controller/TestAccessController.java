package com.example.Learnova.testAccess.controller;


import com.example.Learnova.testAccess.dto.PublishTestRequest;
import com.example.Learnova.testAccess.dto.PublishTestResponse;
import com.example.Learnova.testAccess.service.TestAccessService;
import com.example.Learnova.user.config.customUserDetails.CustomUserDetails;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestAccessController {

    private final TestAccessService testAccessService;

    // GENERATE SHARE LINK
    @PostMapping("/{testId}/publish")
    public PublishTestResponse publishTest(
            @PathVariable Long testId,
            @RequestBody PublishTestRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserInfo user = userDetails.getUser();
        return testAccessService.publishTest(testId, request, user);
    }
}
