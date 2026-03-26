package com.example.Learnova.testPerformer.service;

import com.example.Learnova.exception.ResourceNotFoundException;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.repository.TestQuestionsRepository;
import com.example.Learnova.testPerformer.dto.AttemptReviewResponse;
import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.testPerformer.model.TestAttemptAnswer;
import com.example.Learnova.testPerformer.repository.TestAttemptAnswerRepository;
import com.example.Learnova.testPerformer.repository.TestAttemptRepository;
import com.example.Learnova.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestAttemptAnswerReview {

    private final TestAttemptRepository testAttemptRepository;
    private final TestAttemptAnswerRepository testAttemptAnswerRepository;
    private final TestQuestionsRepository testQuestionsRepository;


    public AttemptReviewResponse getAttemptReview(Long attemptId, UserInfo user) {

        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));

        // Security: only the owner can view their own review
        if (!attempt.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your attempt");
        }

        if (attempt.getEndTime() == null) {
            throw new RuntimeException("Test not yet submitted");
        }

        Tests test = attempt.getTestAccess().getTest();
        int totalQuestions = testQuestionsRepository.countByTest(test);

        List<TestAttemptAnswer> answers =
                testAttemptAnswerRepository.findByAttemptOrderByIdAsc(attempt);

        List<AttemptReviewResponse.AnswerReviewItem> items = answers.stream()
                .map(a -> AttemptReviewResponse.AnswerReviewItem.builder()
                        .questionId(a.getQuestion().getId())
                        .questionText(a.getQuestion().getQuestionText())
                        .optionA(a.getQuestion().getOptionA())
                        .optionB(a.getQuestion().getOptionB())
                        .optionC(a.getQuestion().getOptionC())
                        .optionD(a.getQuestion().getOptionD())
                        .selectedOption(a.getSelectedOption())
                        .correctOption(a.getCorrectOption())
                        .isCorrect(a.getCorrect())
                        .marks(a.getMarks())
                        .build())
                .toList();

        return AttemptReviewResponse.builder()
                .attemptId(attempt.getId())
                .testTitle(test.getTitle())
                .score(attempt.getScore())
                .totalQuestions(totalQuestions)
                .durationMinutes(test.getDurationMinutes())
                .attemptNumber(attempt.getAttemptNumber())
                .startTime(attempt.getStartTime())
                .endTime(attempt.getEndTime())
                .answers(items)
                .build();
    }
}
