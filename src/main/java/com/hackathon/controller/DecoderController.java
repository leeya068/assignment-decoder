package com.hackathon.controller;

import com.hackathon.exception.AssignmentAnalysisException;
import com.hackathon.exception.FileProcessingException;
import com.hackathon.model.AssignmentRequest;
import com.hackathon.model.TaskChecklist;
import com.hackathon.service.BobAnalysisService;
import com.hackathon.service.CodeGeneratorService;
import com.hackathon.util.AppConstants;
import com.hackathon.util.FileUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.*;

/**
 * Main controller for assignment decoder functionality.
 * Handles web requests for assignment analysis and code generation.
 */
@Controller
public class DecoderController {
    
    private static final Logger logger = LoggerFactory.getLogger(DecoderController.class);
    
    private final BobAnalysisService bobService;
    private final CodeGeneratorService codeGenerator;
    
    @Autowired
    public DecoderController(BobAnalysisService bobService, CodeGeneratorService codeGenerator) {
        this.bobService = bobService;
        this.codeGenerator = codeGenerator;
    }
    
    /**
     * Home page - displays assignment submission form
     */
    @GetMapping("/")
    public String home(Model model) {
        logger.debug("Displaying home page");
        model.addAttribute("assignmentRequest", new AssignmentRequest());
        return "index";
    }
    
    /**
     * Analyzes assignment and generates study plan
     */
    @PostMapping("/analyze")
    public String analyzeAssignment(@Valid @ModelAttribute AssignmentRequest request,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        long startTime = System.currentTimeMillis();
        logger.info(AppConstants.LOG_ANALYSIS_START, request.getCourseName());
        
        Path tempDir = null;
        try {
            // Validation
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("error",
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
                return "redirect:/";
            }
            
            if (!request.hasAssignmentContent()) {
                redirectAttributes.addFlashAttribute("error", AppConstants.ERROR_NO_ASSIGNMENT_TEXT);
                return "redirect:/";
            }
            
            // Validate file uploads
            if (request.getAssignmentPdf() != null && !request.getAssignmentPdf().isEmpty()) {
                FileUtils.validateFileSize(request.getAssignmentPdf(), AppConstants.MAX_FILE_SIZE_MB);
                FileUtils.validateFileExtension(request.getAssignmentPdf(), AppConstants.PDF_EXTENSION);
            }
            
            if (request.getStudentCodeZip() != null && !request.getStudentCodeZip().isEmpty()) {
                FileUtils.validateFileSize(request.getStudentCodeZip(), AppConstants.MAX_FILE_SIZE_MB);
                FileUtils.validateFileExtension(request.getStudentCodeZip(), AppConstants.ZIP_EXTENSION);
            }
            
            // Create temporary directory
            tempDir = FileUtils.createTempDirectory();
            logger.debug("Created temp directory: {}", tempDir);
            
            // Extract assignment text from uploaded file or text input
            String assignmentText = request.getAssignmentText();
            if (request.getAssignmentPdf() != null && !request.getAssignmentPdf().isEmpty()) {
                logger.info(AppConstants.LOG_FILE_UPLOAD,
                    request.getAssignmentPdf().getOriginalFilename(),
                    request.getAssignmentPdf().getSize());
                // Use enhanced file reading that supports multiple formats
                assignmentText = FileUtils.extractTextFromFile(request.getAssignmentPdf());
                logger.info("Successfully extracted assignment content from uploaded file");
            }
            
            // Handle student code upload
            File studentCodeDir = null;
            if (request.getStudentCodeZip() != null && !request.getStudentCodeZip().isEmpty()) {
                studentCodeDir = tempDir.resolve(AppConstants.STUDENT_CODE_SUBDIR).toFile();
                studentCodeDir.mkdirs();
                logger.info("Student code directory created: {}", studentCodeDir.getAbsolutePath());
                
                // Extract ZIP file
                File zipFile = FileUtils.saveUploadedFile(
                    request.getStudentCodeZip(),
                    tempDir
                );
                FileUtils.extractZipFile(zipFile, studentCodeDir);
                logger.info("Extracted student code from ZIP");
            }
            
            // STEP 1: Bob analyzes assignment
            logger.info("Step 1: Analyzing assignment with Bob");
            TaskChecklist checklist = bobService.analyzeAssignment(
                assignmentText,
                request.getCourseName(),
                studentCodeDir
            );
            
            // STEP 2: Bob generates missing code stubs
            logger.info("Step 2: Generating code stubs");
            File codeOutput = codeGenerator.generateStubFiles(
                assignmentText,
                tempDir.resolve(AppConstants.GENERATED_CODE_SUBDIR).toString()
            );
            
            // STEP 3: Bob generates test cases
            logger.info("Step 3: Generating test cases");
            File testOutput = codeGenerator.generateTestFiles(
                assignmentText,
                tempDir.resolve(AppConstants.GENERATED_TESTS_SUBDIR).toString()
            );
            
            // STEP 4: Bob creates study schedule
            logger.info("Step 4: Creating study schedule");
            String schedule = bobService.generateStudySchedule(checklist.getTasks(), request.getDueDate());
            
            // Copy generated files to persistent location for download
            Path persistentDir = Paths.get("./generated-output/" + System.currentTimeMillis());
            Files.createDirectories(persistentDir);
            
            Path persistentCodePath = persistentDir.resolve("code");
            Path persistentTestPath = persistentDir.resolve("tests");
            
            FileUtils.copyDirectory(codeOutput.toPath(), persistentCodePath);
            FileUtils.copyDirectory(testOutput.toPath(), persistentTestPath);
            
            // Store results in flash attributes
            redirectAttributes.addFlashAttribute("checklist", checklist);
            redirectAttributes.addFlashAttribute("schedule", schedule);
            redirectAttributes.addFlashAttribute("codePath", persistentCodePath.toString());
            redirectAttributes.addFlashAttribute("testPath", persistentTestPath.toString());
            redirectAttributes.addFlashAttribute("success", AppConstants.SUCCESS_ANALYSIS_COMPLETE);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info(AppConstants.LOG_ANALYSIS_COMPLETE, duration);
            
            return "redirect:/results";
            
        } catch (FileProcessingException e) {
            logger.error("File processing error", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (AssignmentAnalysisException e) {
            logger.error("Analysis error", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Unexpected error during analysis", e);
            redirectAttributes.addFlashAttribute("error",
                "An unexpected error occurred: " + e.getMessage());
            return "redirect:/";
        } finally {
            // Cleanup temporary directory
            if (tempDir != null) {
                FileUtils.deleteDirectory(tempDir.toFile());
                logger.debug("Cleaned up temp directory: {}", tempDir);
            }
        }
    }
    
    /**
     * Results page - displays analysis results
     */
    @GetMapping("/results")
    public String results(Model model) {
        logger.debug("Displaying results page");
        return "results";
    }
    
    /**
     * Evidence page - displays Bob interaction logs
     */
    @GetMapping("/evidence")
    public String evidence(Model model) {
        logger.debug("Loading evidence logs");
        try {
            File logFile = new File("./evidence/bob-logs.txt");
            if (logFile.exists()) {
                String logs = Files.readString(logFile.toPath());
                model.addAttribute("bobLogs", logs);
                logger.info("Loaded {} characters of Bob logs", logs.length());
            } else {
                model.addAttribute("bobLogs", "No logs found yet. Run an analysis first!");
                logger.warn("Bob logs file not found");
            }
        } catch (IOException e) {
            logger.error("Failed to load Bob logs", e);
            model.addAttribute("bobLogs", "Error loading logs: " + e.getMessage());
        }
        return "evidence";
    }
    
    /**
     * Downloads task checklist as CSV
     */
    @GetMapping("/download/checklist")
    public ResponseEntity<String> downloadChecklist(@ModelAttribute("checklist") TaskChecklist checklist) {
        logger.info("Generating checklist CSV download");
        
        if (checklist == null) {
            logger.warn("Checklist is null");
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("No checklist data available. Please analyze an assignment first.");
        }
        
        if (checklist.getTasks() == null || checklist.getTasks().isEmpty()) {
            logger.warn("Checklist has no tasks");
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("No tasks found in checklist");
        }
        
        StringBuilder csv = new StringBuilder();
        csv.append("Task,Priority,Estimated Minutes\n");
        
        for (TaskChecklist.Task task : checklist.getTasks()) {
            csv.append(String.format("\"%s\",\"%s\",\"%s\"\n",
                task.getDescription().replace("\"", "\"\""),
                task.getPriority(),
                task.getEstimatedMinutes()));
        }
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"task-checklist.csv\"")
            .header(HttpHeaders.CONTENT_TYPE, AppConstants.CONTENT_TYPE_CSV)
            .body(csv.toString());
    }

    /**
     * Downloads study schedule as Markdown
     */
    @GetMapping("/download/schedule")
    public ResponseEntity<String> downloadSchedule(@RequestParam String schedule) {
        logger.info("Generating schedule markdown download");
        
        if (schedule == null || schedule.trim().isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("No schedule data available");
        }
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"study-schedule.md\"")
            .header(HttpHeaders.CONTENT_TYPE, AppConstants.CONTENT_TYPE_MARKDOWN)
            .body(schedule);
    }
}