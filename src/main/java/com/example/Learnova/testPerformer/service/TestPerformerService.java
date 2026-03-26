package com.example.Learnova.testPerformer.service;

import com.example.Learnova.exception.BadRequestException;
import com.example.Learnova.exception.ResourceNotFoundException;
import com.example.Learnova.testAccess.model.TestAccess;
import com.example.Learnova.testAccess.repository.TestAccessRepository;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testPerformer.model.TestAttemptAnswer;
import com.example.Learnova.testPerformer.repository.TestAttemptAnswerRepository;
import com.example.Learnova.testCreator.model.TestQuestions;
import com.example.Learnova.testCreator.repository.TestQuestionsRepository;
import com.example.Learnova.testPerformer.dto.*;
import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.testPerformer.repository.TestAttemptRepository;
import com.example.Learnova.user.model.UserInfo;
import com.example.Learnova.user.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TestPerformerService {

    private final TestAccessRepository testAccessRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final TestQuestionsRepository testQuestionsRepository;
    private final TestAttemptAnswerRepository testAttemptAnswerRepository;

    // -------------------------------
    // START TEST (via token)
    // -------------------------------

    public StartTestResponse startTest(String token, UserInfo user) {

        TestAccess access = testAccessRepository
                .findByAccessTokenAndActiveTrue(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or inactive link"));

        validateUserRole(user);
        validateAccess(access);

//      Check active attempt first
        TestAttempt activeAttempt =
                testAttemptRepository
                        .findByTestAccessAndUserAndEndTimeIsNull(access, user);

        if (activeAttempt != null) {
            // Resume existing attempt instead of creating new
            return buildStartResponse(activeAttempt);
        }


//      Count only completed attempts
        int completedAttempts =
                testAttemptRepository
                        .countByTestAccessAndUserAndEndTimeIsNotNull(access, user);

//      Validate attempt limit
        if (access.getMaxAttempts() != null &&
                completedAttempts >= access.getMaxAttempts()) {
            throw new BadRequestException("Max attempts reached");
        }


//      Create new attempt
        TestAttempt attempt = new TestAttempt();
        attempt.setTestAccess(access);
        attempt.setUser(user);
        attempt.setAttemptNumber(completedAttempts + 1);
        attempt.setStartTime(LocalDateTime.now());

        testAttemptRepository.save(attempt);
        // Fetch Questions
        List<TestQuestions> testQuestions =
                testQuestionsRepository.findByTest(access.getTest());

        List<QuestionResponse> questions = testQuestions.stream()
                .map(tq -> QuestionResponse.builder()
                        .questionId(tq.getQuestion().getId())
                        .questionText(tq.getQuestion().getQuestionText())
                        .optionA(tq.getQuestion().getOptionA())
                        .optionB(tq.getQuestion().getOptionB())
                        .optionC(tq.getQuestion().getOptionC())
                        .optionD(tq.getQuestion().getOptionD())
                        .build())
                .toList();

        return StartTestResponse.builder()
                .attemptId(attempt.getId())
                .testTitle(access.getTest().getTitle())
                .durationMinutes(access.getTest().getDurationMinutes())
                .questions(questions)
                .build();
    }

    // -------------------------------
    // SUBMIT TEST
    // -------------------------------
    public SubmitTestResponse submitTest(
            Long attemptId,
            SubmitTestRequest request,
            UserInfo user
    ) {

        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));

        if (!attempt.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (attempt.getEndTime() != null) {
            throw new RuntimeException("Already submitted");
        }

        List<TestQuestions> testQuestions =
                testQuestionsRepository.findByTest(
                        attempt.getTestAccess().getTest()
                );

        int totalScore = 0;

        for (TestQuestions tq : testQuestions) {

            Long qId = tq.getQuestion().getId();
            String selected = request.getAnswers().get(qId);
            String correct = tq.getQuestion().getCorrectAnswer();

            boolean isCorrect =
                    selected != null && selected.equalsIgnoreCase(correct);

            int marks = isCorrect ? tq.getMarks() : 0;
            totalScore += marks;

            //SAVE ANSWER
            TestAttemptAnswer answer = new TestAttemptAnswer();
            answer.setAttempt(attempt);
            answer.setQuestion(tq.getQuestion());
            answer.setSelectedOption(selected);
            answer.setCorrectOption(correct);
            answer.setCorrect(isCorrect);
            answer.setMarks(marks);

            testAttemptAnswerRepository.save(answer);
        }

        attempt.setScore(totalScore);
        attempt.setEndTime(LocalDateTime.now());

        testAttemptRepository.save(attempt);

        return SubmitTestResponse.builder()
                .attemptId(attempt.getId())
                .score(totalScore)
                .message("Test submitted successfully")
                .build();
    }
    // -------------------------------
    // VALIDATION
    // -------------------------------
    private void validateAccess(TestAccess access) {

        LocalDateTime now = LocalDateTime.now();

        if (access.getAvailableFrom() != null &&
                now.isBefore(access.getAvailableFrom())) {
            throw new RuntimeException("Test not started yet");
        }

        if (access.getAvailableUntil() != null &&
                now.isAfter(access.getAvailableUntil())) {
            throw new RuntimeException("Test expired");
        }
    }
    //----------------------
    //USER ROLE VERIFICATION
    //---------------------
    private void validateUserRole(UserInfo user) {

        if (user.getRole() == Role.FACULTY) {
            throw new AccessDeniedException("Faculty members are not allowed to take tests");
        }
    }

//    -----------------------------------------------------
    public List<MyResultResponse> getMyResults(UserInfo user) {

        List<TestAttempt> attempts =
                testAttemptRepository.findCompletedByUser(user);

        return attempts.stream().map(attempt -> {

            Tests test = attempt.getTestAccess().getTest();

            // Count total questions for this test
            int totalQuestions =
                    testQuestionsRepository.countByTest(test);

            return MyResultResponse.builder()
                    .attemptId(attempt.getId())
                    .testTitle(test.getTitle())
                    .score(attempt.getScore())
                    .totalQuestions(totalQuestions)
                    .durationMinutes(test.getDurationMinutes())
                    .attemptNumber(attempt.getAttemptNumber())
                    .startTime(attempt.getStartTime())
                    .endTime(attempt.getEndTime())
                    .build();

        }).toList();
    }

    private StartTestResponse buildStartResponse(TestAttempt attempt) {

        List<TestQuestions> testQuestions =
                testQuestionsRepository.findByTest(
                        attempt.getTestAccess().getTest());

        List<QuestionResponse> questions = testQuestions.stream()
                .map(tq -> QuestionResponse.builder()
                        .questionId(tq.getQuestion().getId())
                        .questionText(tq.getQuestion().getQuestionText())
                        .optionA(tq.getQuestion().getOptionA())
                        .optionB(tq.getQuestion().getOptionB())
                        .optionC(tq.getQuestion().getOptionC())
                        .optionD(tq.getQuestion().getOptionD())
                        .build())
                .toList();

        return StartTestResponse.builder()
                .attemptId(attempt.getId())
                .testTitle(attempt.getTestAccess().getTest().getTitle())
                .durationMinutes(attempt.getTestAccess().getTest().getDurationMinutes())
                .questions(questions)
                .build();
    }
}
