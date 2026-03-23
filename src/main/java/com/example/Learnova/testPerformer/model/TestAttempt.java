package com.example.Learnova.testPerformer.model;

import com.example.Learnova.testAccess.model.TestAccess;
import com.example.Learnova.user.model.UserInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to access (important for token + rules)
    @ManyToOne
    @JoinColumn(name = "test_access_id")
    private TestAccess testAccess;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    private Integer attemptNumber;

    private Integer score;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}