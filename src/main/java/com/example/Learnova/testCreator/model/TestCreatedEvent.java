package com.example.Learnova.testCreator.model;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;

public record TestCreatedEvent(
        Long testId,
        GenerateTestRequest request
) {}