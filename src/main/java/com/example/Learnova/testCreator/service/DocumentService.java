package com.example.Learnova.testCreator.service;

import com.example.Learnova.exception.BadRequestException;
import com.example.Learnova.exception.FileProcessingException;
import com.example.Learnova.testCreator.dto.DocumentUploadResponse;
import com.example.Learnova.testCreator.model.Documents;
import com.example.Learnova.testCreator.model.enums.Status;
import com.example.Learnova.testCreator.parser.DocxParser;
import com.example.Learnova.testCreator.parser.PdfParser;
import com.example.Learnova.testCreator.repository.DocumentsRepository;
import com.example.Learnova.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private PdfParser pdfParser;

    @Autowired
    private DocxParser docxParser;

    public DocumentUploadResponse uploadDocument(MultipartFile file, UserInfo user) throws Exception {

        // 1. Validate file
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new BadRequestException("Invalid file name");
        }

        if (!originalFileName.contains(".")) {
            throw new BadRequestException("File extension missing");
        }

        // 2. Detect file type
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        if (!extension.equals("pdf") && !extension.equals("docx")) {
            throw new BadRequestException("Only PDF and DOCX files are allowed");
        }

        // 3. Save file locally
        try {

            // 3. Save file locally
            String newFileName = UUID.randomUUID() + "_" + originalFileName;
            Path filePath = Paths.get(uploadDir, newFileName);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            File savedFile = filePath.toFile();

            // 4. Extract text
            String extractedText;

            if (extension.equals("pdf")) {
                extractedText = pdfParser.extractText(savedFile);
            } else {
                extractedText = docxParser.extractText(savedFile);
            }

            if (extractedText == null || extractedText.isBlank()) {
                throw new FileProcessingException("No text could be extracted from file");
            }

            // 5. Save to DB
            Documents document = new Documents();
            document.setFileName(originalFileName);
            document.setFileType(extension.toUpperCase());
            document.setFilePath(filePath.toString());
            document.setExtractedText(extractedText);
            document.setUploadedBy(user);
            document.setUploadedAt(LocalDateTime.now());
            document.setStatus(Status.COMPLETED);

            Documents savedDocument = documentsRepository.save(document);

            return new DocumentUploadResponse(
                    savedDocument.getId(),
                    "Document uploaded successfully"
            );

        } catch (FileProcessingException ex) {
            throw ex; // rethrow custom exception
        } catch (Exception ex) {
            throw new FileProcessingException("Failed to process uploaded file");
        }
    }
}
