package com.example.Learnova.testCreator.repository;

import com.example.Learnova.testCreator.model.Tests;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestsRepository extends JpaRepository<Tests,Long> {
}
