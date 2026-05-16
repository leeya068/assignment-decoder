package com.hackathon.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handles assignment analysis exceptions for web pages
     */
    @ExceptionHandler(AssignmentAnalysisException.class)
    public String handleAssignmentAnalysisException(
            AssignmentAnalysisException ex,
            RedirectAttributes redirectAttributes) {
        logger.error("Assignment analysis failed: {} (Code: {})", ex.getMessage(), ex.getErrorCode(), ex);
        redirectAttributes.addFlashAttribute("error",
            "Analysis failed: " + ex.getMessage());
        return "redirect:/";
    }
    
    /**
     * Handles file processing exceptions
     */
    @ExceptionHandler(FileProcessingException.class)
    public String handleFileProcessingException(
            FileProcessingException ex,
            RedirectAttributes redirectAttributes) {
        logger.error("File processing failed for {}: {}", ex.getFileName(), ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("error", 
            "Failed to process file '" + ex.getFileName() + "': " + ex.getMessage());
        return "redirect:/";
    }
    
    /**
     * Handles file size exceeded exceptions
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex,
            RedirectAttributes redirectAttributes) {
        logger.error("File size exceeded: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", 
            "File size exceeds maximum allowed (10MB). Please upload a smaller file.");
        return "redirect:/";
    }
    
    /**
     * Handles validation exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(
            IllegalArgumentException ex,
            RedirectAttributes redirectAttributes) {
        logger.error("Validation error: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", 
            "Invalid input: " + ex.getMessage());
        return "redirect:/";
    }
    
    /**
     * Handles all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(
            Exception ex,
            RedirectAttributes redirectAttributes) {
        logger.error("Unexpected error occurred", ex);
        redirectAttributes.addFlashAttribute("error", 
            "An unexpected error occurred. Please try again or contact support.");
        return "redirect:/";
    }
    
}

// Made with Bob
