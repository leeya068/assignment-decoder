package com.hackathon.controller;

import com.hackathon.model.AssignmentRequest;
import com.hackathon.model.TaskChecklist;
import com.hackathon.service.BobAnalysisService;
import com.hackathon.service.CodeGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for DecoderController.
 * Tests web request handling and view rendering.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Decoder Controller Tests")
class DecoderControllerTest {
    
    @Mock
    private BobAnalysisService bobService;
    
    @Mock
    private CodeGeneratorService codeGenerator;
    
    @InjectMocks
    private DecoderController controller;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        // Setup view resolver for testing
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(viewResolver)
            .build();
    }
    
    @Test
    @DisplayName("Should display home page")
    void testHome() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("assignmentRequest"));
    }
    
    @Test
    @DisplayName("Should display results page")
    void testResults() throws Exception {
        mockMvc.perform(get("/results"))
            .andExpect(status().isOk())
            .andExpect(view().name("results"));
    }
    
    @Test
    @DisplayName("Should display evidence page")
    void testEvidence() throws Exception {
        mockMvc.perform(get("/evidence"))
            .andExpect(status().isOk())
            .andExpect(view().name("evidence"))
            .andExpect(model().attributeExists("bobLogs"));
    }
    
    @Test
    @DisplayName("Should handle assignment analysis request")
    void testAnalyzeAssignment_Success() throws Exception {
        // Given
        TaskChecklist mockChecklist = new TaskChecklist();
        mockChecklist.setAssignmentName("Test Assignment");
        
        when(bobService.analyzeAssignment(any(), any(), any()))
            .thenReturn(mockChecklist);
        when(bobService.generateStudySchedule(any(), any()))
            .thenReturn("Mock Schedule");
        
        // When/Then
        mockMvc.perform(post("/analyze")
                .param("assignmentText", "Build a REST API")
                .param("courseName", "CS101")
                .param("dueDate", "2026-05-20"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/results"));
        
        verify(bobService, times(1)).analyzeAssignment(any(), any(), any());
    }
    
    @Test
    @DisplayName("Should reject empty assignment text")
    void testAnalyzeAssignment_EmptyText() throws Exception {
        mockMvc.perform(post("/analyze")
                .param("assignmentText", "")
                .param("courseName", "CS101")
                .param("dueDate", "2026-05-20"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
        
        verify(bobService, never()).analyzeAssignment(any(), any(), any());
    }
    
    @Test
    @DisplayName("Should download checklist as CSV")
    void testDownloadChecklist() throws Exception {
        // Given
        TaskChecklist checklist = new TaskChecklist();
        TaskChecklist.Task task = new TaskChecklist.Task("Test task", "HIGH", "30");
        checklist.getTasks().add(task);
        
        // When/Then
        mockMvc.perform(get("/download/checklist")
                .flashAttr("checklist", checklist))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", 
                "attachment; filename=\"task-checklist.csv\""))
            .andExpect(content().contentType("text/csv"));
    }
    
    @Test
    @DisplayName("Should download schedule as Markdown")
    void testDownloadSchedule() throws Exception {
        // Given
        String schedule = "# Study Schedule\n## Day 1\n- Task 1";
        
        // When/Then
        mockMvc.perform(get("/download/schedule")
                .param("schedule", schedule))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", 
                "attachment; filename=\"study-schedule.md\""))
            .andExpect(content().contentType("text/markdown"));
    }
}

// Made with Bob
