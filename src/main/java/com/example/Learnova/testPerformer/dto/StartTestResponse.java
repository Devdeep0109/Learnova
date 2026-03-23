package com.example.Learnova.testPerformer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StartTestResponse {

    private Long attemptId;
    private String testTitle;
    private Integer durationMinutes;

    private List<QuestionResponse> questions;
}
