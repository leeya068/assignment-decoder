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
        Files.createDirectories(Paths.get(outputPath));
        
        // Bob generates the code stub
        var codeStubs = bobService.generateCodeStubs(requirement, "java");
        
        for (int i = 0; i < codeStubs.size(); i++) {
            String filename = outputPath + "/GeneratedStub_" + (i+1) + ".java";
            Files.writeString(Paths.get(filename), codeStubs.get(i));
        }
        
        return new File(outputPath);
    }
    
    public File generateTestFiles(String requirement, String outputPath) throws IOException {
        Files.createDirectories(Paths.get(outputPath));
        
        var testCases = bobService.generateTestCases(requirement, java.util.List.of("Valid input", "Edge cases"));
        
        for (int i = 0; i < testCases.size(); i++) {
            String filename = outputPath + "/GeneratedTest_" + (i+1) + ".java";
            Files.writeString(Paths.get(filename), testCases.get(i));
        }
        
        return new File(outputPath);
    }
}
