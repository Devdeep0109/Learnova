package com.example.Learnova.testPerformer.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SubmitTestRequest {

    // questionId -> selectedOption (A/B/C/D)
    private Map<Long, String> answers;
}