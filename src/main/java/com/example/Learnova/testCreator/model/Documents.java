package com.example.Learnova.testCreator.model;

import com.example.Learnova.testCreator.model.enums.Status;
import com.example.Learnova.user.model.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType; // PDF, DOCX

    private String fileName;

    private String filePath;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT") // this is used to define that I can have long format string.........
    private String extractedText; // filled later by AI

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private UserInfo uploadedBy;

    private LocalDateTime uploadedAt;

}
