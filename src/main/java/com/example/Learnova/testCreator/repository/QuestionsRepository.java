package com.example.Learnova.testCreator.repository;

import com.example.Learnova.testCreator.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {
}
