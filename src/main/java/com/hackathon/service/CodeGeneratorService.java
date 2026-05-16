package com.hackathon.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;

@Service
public class CodeGeneratorService {
    
    private final BobAnalysisService bobService;
    
    public CodeGeneratorService(BobAnalysisService bobService) {
        this.bobService = bobService;
    }
    
    public File generateStubFiles(String requirement, String outputPath) throws IOException {
        Path outputDir = Paths.get(outputPath);
        Files.createDirectories(outputDir);
        
        // Bob generates the code stub
        var codeStubs = bobService.generateCodeStubs(requirement, "java");
        
        for (int i = 0; i < codeStubs.size(); i++) {
            Path filename = outputDir.resolve("GeneratedStub_" + (i+1) + ".java");
            Files.writeString(filename, codeStubs.get(i));
        }
        
        return outputDir.toFile();
    }
    
    public File generateTestFiles(String requirement, String outputPath) throws IOException {
        Path outputDir = Paths.get(outputPath);
        Files.createDirectories(outputDir);
        
        var testCases = bobService.generateTestCases(requirement, java.util.List.of("Valid input", "Edge cases"));
        
        for (int i = 0; i < testCases.size(); i++) {
            Path filename = outputDir.resolve("GeneratedTest_" + (i+1) + ".java");
            Files.writeString(filename, testCases.get(i));
        }
        
        return outputDir.toFile();
    }
}
