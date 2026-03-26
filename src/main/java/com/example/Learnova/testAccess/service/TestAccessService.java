
package com.example.Learnova.testAccess.service;

import com.example.Learnova.exception.ResourceNotFoundException;
import com.example.Learnova.testAccess.dto.PublishTestRequest;
import com.example.Learnova.testAccess.dto.PublishTestResponse;
import com.example.Learnova.testAccess.model.TestAccess;
import com.example.Learnova.testAccess.repository.TestAccessRepository;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.repository.TestsRepository;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestAccessService {

    private final TestsRepository testsRepository;
    private final TestAccessRepository testAccessRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public PublishTestResponse publishTest(
            Long testId,PublishTestRequest request, UserInfo publisher) {



        // 1. Validate Test
        Tests test = testsRepository.findById(testId)
                    .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        if (request.getMaxAttempts() == null || request.getMaxAttempts() < 1) {
            throw new IllegalArgumentException("Max attempts must be at least 1");
        }

        // 2. Create Access
        TestAccess access = new TestAccess();
        access.setTest(test);
        access.setPublishedBy(publisher);
        access.setAvailableFrom(request.getAvailableFrom());
        access.setAvailableUntil(request.getAvailableUntil());
        access.setMaxAttempts(request.getMaxAttempts());
        access.setActive(true);

        testAccessRepository.save(access);

        // 3. Build Share Link
        String shareLink =
                baseUrl + "/api/tests/join/" + access.getAccessToken();

        // 4. Response
        return PublishTestResponse.builder()
                .accessId(access.getId())
                .accessToken(access.getAccessToken())
                .shareLink(shareLink)
                .availableFrom(access.getAvailableFrom())
                .availableUntil(access.getAvailableUntil())
                .maxAttempts(access.getMaxAttempts())
                .active(access.getActive())
                .build();
    }
}
