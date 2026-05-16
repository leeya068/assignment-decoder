package com.hackathon.service;

import com.hackathon.model.TaskChecklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Async wrapper service for Bob API calls.
 * Enables non-blocking execution of long-running analysis tasks.
 */
@Service
public class AsyncBobService {
    
    private static final Logger logger = LoggerFactory.getLogger(AsyncBobService.class);
    
    private final BobAnalysisService bobService;
    
    @Autowired
    public AsyncBobService(BobAnalysisService bobService) {
        this.bobService = bobService;
    }
    
    /**
     * Asynchronously analyzes assignment.
     * Returns a CompletableFuture that completes when analysis is done.
     */
    @Async("bobTaskExecutor")
    public CompletableFuture<TaskChecklist> analyzeAssignmentAsync(
            String assignmentText, 
            String courseName, 
            File studentCodeFolder) {
        
        logger.info("Starting async assignment analysis for: {}", courseName);
        long startTime = System.currentTimeMillis();
        
        try {
            TaskChecklist result = bobService.analyzeAssignment(
                assignmentText, courseName, studentCodeFolder
            );
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Async analysis completed in {}ms", duration);
            
            return CompletableFuture.completedFuture(result);
            
        } catch (Exception e) {
            logger.error("Async analysis failed", e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Asynchronously generates code stubs.
     */
    @Async("bobTaskExecutor")
    public CompletableFuture<List<String>> generateCodeStubsAsync(
            String requirement, 
            String language) {
        
        logger.info("Starting async code stub generation");
        
        try {
            List<String> stubs = bobService.generateCodeStubs(requirement, language);
            logger.info("Generated {} code stubs", stubs.size());
            return CompletableFuture.completedFuture(stubs);
        } catch (Exception e) {
            logger.error("Code stub generation failed", e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Asynchronously generates test cases.
     */
    @Async("bobTaskExecutor")
    public CompletableFuture<List<String>> generateTestCasesAsync(
            String requirement, 
            List<String> expectedOutputs) {
        
        logger.info("Starting async test case generation");
        
        try {
            List<String> tests = bobService.generateTestCases(requirement, expectedOutputs);
            logger.info("Generated {} test cases", tests.size());
            return CompletableFuture.completedFuture(tests);
        } catch (Exception e) {
            logger.error("Test case generation failed", e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Asynchronously generates study schedule.
     */
    @Async("bobTaskExecutor")
    public CompletableFuture<String> generateStudyScheduleAsync(
            List<TaskChecklist.Task> tasks, 
            String dueDate) {
        
        logger.info("Starting async schedule generation");
        
        try {
            String schedule = bobService.generateStudySchedule(tasks, dueDate);
            logger.info("Schedule generated successfully");
            return CompletableFuture.completedFuture(schedule);
        } catch (Exception e) {
            logger.error("Schedule generation failed", e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Executes all Bob tasks in parallel and waits for completion.
     * Returns when all tasks are done.
     */
    public CompletableFuture<Void> executeAllTasksParallel(
            String assignmentText,
            String courseName,
            File studentCodeFolder,
            String dueDate) {
        
        logger.info("Starting parallel execution of all Bob tasks");
        
        CompletableFuture<TaskChecklist> analysisFuture = 
            analyzeAssignmentAsync(assignmentText, courseName, studentCodeFolder);
        
        CompletableFuture<List<String>> codeStubsFuture = 
            generateCodeStubsAsync(assignmentText, "java");
        
        CompletableFuture<List<String>> testCasesFuture = 
            generateTestCasesAsync(assignmentText, List.of("Valid input", "Edge cases"));
        
        // Wait for analysis to complete, then generate schedule
        CompletableFuture<String> scheduleFuture = analysisFuture.thenCompose(checklist ->
            generateStudyScheduleAsync(checklist.getTasks(), dueDate)
        );
        
        // Combine all futures
        return CompletableFuture.allOf(
            analysisFuture, 
            codeStubsFuture, 
            testCasesFuture, 
            scheduleFuture
        ).thenRun(() -> logger.info("All Bob tasks completed successfully"));
    }
}

// Made with Bob
