package com.example.Learnova.testPerformer.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MyResultResponse {
    private Long attemptId;
    private String testTitle;
    private Integer score;
    private Integer totalQuestions;
    private Integer durationMinutes;
    private Integer attemptNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}