package com.example.Learnova.testCreator.dto;

import lombok.Data;

@Data
public class GenerateTestRequest {

    private Long documentId;
    private int numberOfQuestions;
    private String focusTopic;
    private String difficulty;
    private String title;
    private int durationMinutes;
}
