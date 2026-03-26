package com.example.Learnova.testCreator.model;

import com.example.Learnova.testCreator.model.enums.Status;
import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.user.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Tests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer durationMinutes;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonBackReference
    private UserInfo createdBy;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;


}
