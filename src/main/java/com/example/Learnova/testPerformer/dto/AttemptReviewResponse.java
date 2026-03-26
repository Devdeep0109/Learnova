package com.example.Learnova.testPerformer.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AttemptReviewResponse {

    private Long attemptId;
    private String testTitle;
    private Integer score;
    private Integer totalQuestions;
    private Integer durationMinutes;
    private Integer attemptNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<AnswerReviewItem> answers;

    @Data
    @Builder
    public static class AnswerReviewItem {
        private Long questionId;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String selectedOption;  // what the student picked (null if skipped)
        private String correctOption;   // the right answer
        private Boolean isCorrect;
        private Integer marks;
    }
}