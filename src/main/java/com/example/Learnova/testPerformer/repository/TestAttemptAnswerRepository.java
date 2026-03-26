package com.example.Learnova.testPerformer.repository;

import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.testPerformer.model.TestAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestAttemptAnswerRepository
        extends JpaRepository<TestAttemptAnswer, Long> {

    List<TestAttemptAnswer> findByAttemptOrderByIdAsc(TestAttempt attempt);

}