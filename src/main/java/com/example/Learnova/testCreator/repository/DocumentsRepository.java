package com.example.Learnova.testCreator.repository;

import com.example.Learnova.testCreator.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {
}
