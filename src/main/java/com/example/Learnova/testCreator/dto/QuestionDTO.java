package com.example.Learnova.testCreator.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {

    private String question;
    private List<String> options;
    private String correctAnswer;
    private String difficulty;
}
