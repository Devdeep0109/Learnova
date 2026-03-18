package com.example.Learnova.testCreator.dto;

import lombok.Data;

import java.util.List;

@Data
public class McqResponse {
    private List<QuestionDTO> questions;
}
