package com.hackathon.service;

import com.hackathon.model.TaskChecklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BobAnalysisService.
 * Tests Bob API interactions and response parsing.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Bob Analysis Service Tests")
class BobAnalysisServiceTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private BobAnalysisService bobService;
    
    private String sampleAssignmentText;
    private String sampleCourseName;
    
    @BeforeEach
    void setUp() {
        sampleAssignmentText = "Build a REST API with CRUD operations";
        sampleCourseName = "CS101 - Software Engineering";
    }
    
    @Test
    @DisplayName("Should analyze assignment and return task checklist")
    void testAnalyzeAssignment_Success() {
        // Given
        File studentCodeFolder = null;
        
        // When
        TaskChecklist result = bobService.analyzeAssignment(
            sampleAssignmentText, 
            sampleCourseName, 
            studentCodeFolder
        );
        
        // Then
        assertNotNull(result, "Checklist should not be null");
        assertNotNull(result.getTasks(), "Tasks list should not be null");
        assertFalse(result.getTasks().isEmpty(), "Tasks list should not be empty");
        assertTrue(result.getAssignmentName().contains(sampleCourseName), 
            "Assignment name should contain course name");
    }
    
    @Test
    @DisplayName("Should handle null assignment text gracefully")
    void testAnalyzeAssignment_NullText() {
        // Given
        String nullText = null;
        
        // When/Then
        assertThrows(Exception.class, () -> {
            bobService.analyzeAssignment(nullText, sampleCourseName, null);
        }, "Should throw exception for null assignment text");
    }
    
    @Test
    @DisplayName("Should generate code stubs for requirement")
    void testGenerateCodeStubs_Success() {
        // Given
        String requirement = "Create a User class with validation";
        String language = "java";
        
        // When
        List<String> stubs = bobService.generateCodeStubs(requirement, language);
        
        // Then
        assertNotNull(stubs, "Code stubs should not be null");
        assertFalse(stubs.isEmpty(), "Should generate at least one code stub");
    }
    
    @Test
    @DisplayName("Should generate test cases for requirement")
    void testGenerateTestCases_Success() {
        // Given
        String requirement = "Validate user email format";
        List<String> expectedOutputs = List.of("Valid email", "Invalid email");
        
        // When
        List<String> testCases = bobService.generateTestCases(requirement, expectedOutputs);
        
        // Then
        assertNotNull(testCases, "Test cases should not be null");
        assertFalse(testCases.isEmpty(), "Should generate at least one test case");
    }
    
    @Test
    @DisplayName("Should generate study schedule from tasks")
    void testGenerateStudySchedule_Success() {
        // Given
        TaskChecklist checklist = bobService.analyzeAssignment(
            sampleAssignmentText, sampleCourseName, null
        );
        String dueDate = "2026-05-20";
        
        // When
        String schedule = bobService.generateStudySchedule(checklist.getTasks(), dueDate);
        
        // Then
        assertNotNull(schedule, "Schedule should not be null");
        assertFalse(schedule.trim().isEmpty(), "Schedule should not be empty");
        assertTrue(schedule.contains("Day"), "Schedule should contain day breakdown");
    }
    
    @Test
    @DisplayName("Should estimate total time correctly")
    void testEstimateTotalTime() {
        // Given
        TaskChecklist checklist = bobService.analyzeAssignment(
            sampleAssignmentText, sampleCourseName, null
        );
        
        // When
        String estimatedTime = checklist.getEstimatedTimeHours();
        
        // Then
        assertNotNull(estimatedTime, "Estimated time should not be null");
        assertTrue(estimatedTime.contains("hour") || estimatedTime.contains("minute"), 
            "Estimated time should contain time units");
    }
    
    @Test
    @DisplayName("Should parse Bob response to tasks correctly")
    void testParseBobResponseToTasks() {
        // Given
        String mockBobResponse = """
            {
              "tasks": [
                {"description": "Task 1", "priority": "HIGH", "minutes": 30},
                {"description": "Task 2", "priority": "MEDIUM", "minutes": 45}
              ]
            }
            """;
        
        // When
        TaskChecklist checklist = bobService.analyzeAssignment(
            sampleAssignmentText, sampleCourseName, null
        );
        
        // Then
        assertNotNull(checklist.getTasks());
        assertTrue(checklist.getTasks().size() > 0, "Should have parsed tasks");
    }
}

// Made with Bob
