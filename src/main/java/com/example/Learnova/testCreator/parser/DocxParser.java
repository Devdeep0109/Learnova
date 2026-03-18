package com.example.Learnova.testCreator.parser;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Component
public class DocxParser implements DocumentParser {

    @Override
    public String extractText(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument doc = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {

            return extractor.getText();
        }
    }
}