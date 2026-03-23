package com.example.Learnova.testPerformer.repository;

import com.example.Learnova.testPerformer.model.TestAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAttemptAnswerRepository
        extends JpaRepository<TestAttemptAnswer, Long> {
}