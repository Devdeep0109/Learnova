package com.example.Learnova.testCreator.service;

import com.example.Learnova.exception.ResourceNotFoundException;
import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.dto.McqResponse;
import com.example.Learnova.testCreator.model.Documents;
import com.example.Learnova.testCreator.model.TestCreatedEvent;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.model.enums.Status;
import com.example.Learnova.testCreator.repository.DocumentsRepository;
import com.example.Learnova.testCreator.repository.TestsRepository;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Transactional
public class TestGeneratorService {

    private final TestsRepository testsRepo;
    private final ApplicationEventPublisher eventPublisher;

    public Tests createTest(GenerateTestRequest req, UserInfo user) {

        Tests test = new Tests();
        test.setTitle(req.getTitle());
        test.setDurationMinutes(req.getDurationMinutes());
        test.setCreatedBy(user);
        test.setCreatedAt(LocalDateTime.now());
        test.setStatus(Status.CREATED);

        testsRepo.save(test);

        //publish event (NOT async yet)
        eventPublisher.publishEvent(
                new TestCreatedEvent(test.getId(), req)
        );

        return test;
    }
}