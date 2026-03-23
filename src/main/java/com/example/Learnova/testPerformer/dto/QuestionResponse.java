package com.example.Learnova.testPerformer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponse {

    private Long questionId;
    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
}