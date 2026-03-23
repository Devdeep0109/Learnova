package com.example.Learnova.testAccess.model;

import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.user.model.UserInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class TestAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which test is shared
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Tests test;

    // Who published
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_by", nullable = false)
    private UserInfo publishedBy;

    // Public token
    @Column(unique = true, nullable = false, updatable = false)
    private String accessToken;

    // Scheduling
    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;

    // Attempt limit
    private Integer maxAttempts;

    // Active flag
    private Boolean active = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void generateToken() {
        if (this.accessToken == null) {
            this.accessToken = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }
}