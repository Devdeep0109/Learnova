package com.example.Learnova.testCreator.service;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.repository.TestsRepository;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestCreationService {

    private final TestsRepository testsRepo;

    public Tests createTest(GenerateTestRequest req, UserInfo user) {

        Tests test = new Tests();
        test.setTitle(req.getTitle());
        test.setDurationMinutes(req.getDurationMinutes());
        test.setCreatedBy(user);
        test.setCreatedAt(LocalDateTime.now());

        return testsRepo.save(test);
    }
}