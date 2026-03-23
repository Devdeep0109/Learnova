package com.example.Learnova.testCreator.repository;

import com.example.Learnova.testCreator.model.TestQuestions;
import com.example.Learnova.testCreator.model.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestQuestionsRepository extends JpaRepository<TestQuestions,Long> {
    List<TestQuestions> findByTest(Tests test);
}
