package com.hackathon.controller;

import com.hackathon.model.AssignmentRequest;
import com.hackathon.model.TaskChecklist;
import com.hackathon.service.BobAnalysisService;
import com.hackathon.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.*;

@Controller
public class DecoderController {
    
    @Autowired
    private BobAnalysisService bobService;
    
    @Autowired
    private CodeGeneratorService codeGenerator;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("assignmentRequest", new AssignmentRequest());
        return "index";
    }
    
    @PostMapping("/analyze")
    public String analyzeAssignment(@ModelAttribute AssignmentRequest request,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        try {
            // Validation
            if (request.getAssignmentText() == null || request.getAssignmentText().trim().isEmpty()) {
                if (request.getAssignmentPdf() == null || request.getAssignmentPdf().isEmpty()) {
                    redirectAttributes.addFlashAttribute("error",
                        "Please provide assignment text or upload a PDF file.");
                    return "redirect:/";
                }
            }
            
            if (request.getCourseName() == null || request.getCourseName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                    "Please provide a course name.");
                return "redirect:/";
            }
            
            if (request.getDueDate() == null || request.getDueDate().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                    "Please provide a due date.");
                return "redirect:/";
            }
            
            // Save uploaded files temporarily
            String tempDir = "./temp/" + System.currentTimeMillis();
            Files.createDirectories(Paths.get(tempDir));
            
            String assignmentText = request.getAssignmentText();
            
            // If PDF uploaded, extract text (simplified for demo)
            if (request.getAssignmentPdf() != null && !request.getAssignmentPdf().isEmpty()) {
                try {
                    assignmentText = extractTextFromPdf(request.getAssignmentPdf());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("error",
                        "Failed to extract text from PDF: " + e.getMessage());
                    return "redirect:/";
                }
            }
            
            // Save student code if uploaded
            File studentCodeDir = null;
            if (request.getStudentCodeZip() != null && !request.getStudentCodeZip().isEmpty()) {
                studentCodeDir = new File(tempDir + "/student-code");
                studentCodeDir.mkdirs();
                // In real implementation, unzip here
            }
            
            // STEP 1: Bob analyzes assignment
            TaskChecklist checklist = bobService.analyzeAssignment(
                assignmentText,
                request.getCourseName(),
                studentCodeDir
            );
            
            // STEP 2: Bob generates missing code stubs
            File codeOutput = codeGenerator.generateStubFiles(assignmentText, tempDir + "/generated-code");
            
            // STEP 3: Bob generates test cases
            File testOutput = codeGenerator.generateTestFiles(assignmentText, tempDir + "/generated-tests");
            
            // STEP 4: Bob creates study schedule
            String schedule = bobService.generateStudySchedule(checklist.getTasks(), request.getDueDate());
            
            redirectAttributes.addFlashAttribute("checklist", checklist);
            redirectAttributes.addFlashAttribute("schedule", schedule);
            redirectAttributes.addFlashAttribute("codePath", codeOutput.getPath());
            redirectAttributes.addFlashAttribute("testPath", testOutput.getPath());
            redirectAttributes.addFlashAttribute("success",
                "Assignment analyzed successfully! IBM Bob has generated your personalized study plan.");
            
            return "redirect:/results";
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                "An error occurred while analyzing the assignment: " + e.getMessage());
            return "redirect:/";
        }
    }
    
    @GetMapping("/results")
    public String results(Model model) {
        return "results";
    }
    
    @GetMapping("/evidence")
    public String evidence(Model model) {
        // Load Bob interaction logs for display
        try {
            File logFile = new File("./evidence/bob-logs.txt");
            if (logFile.exists()) {
                String logs = Files.readString(logFile.toPath());
                model.addAttribute("bobLogs", logs);
            }
        } catch (Exception e) {
            model.addAttribute("bobLogs", "No logs found yet. Run an analysis first!");
        }
        return "evidence";
    }
    
    private String extractTextFromPdf(MultipartFile pdfFile) throws IOException {
        // Simplified - in production use PDFBox
        return "Sample assignment: Build a REST API with CRUD operations for a Student Management System. " +
               "Requirements: Create Student model, implement GET/POST/PUT/DELETE endpoints, " +
               "add validation for email and age, write unit tests achieving 80% coverage.";
    }
}