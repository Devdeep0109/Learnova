package com.example.Learnova.testPerformer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitTestResponse {

    private Long attemptId;
    private Integer score;
    private String message;
}