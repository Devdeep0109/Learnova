package com.example.Learnova.testCreator.repository;

import com.example.Learnova.testCreator.model.TestQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionsRepository extends JpaRepository<TestQuestions,Long> {
}
