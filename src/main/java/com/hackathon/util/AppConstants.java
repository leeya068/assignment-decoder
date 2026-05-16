package com.hackathon.util;

/**
 * Application-wide constants.
 * Centralizes magic strings and numbers for better maintainability.
 */
public final class AppConstants {
    
    private AppConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // File Processing
    public static final String TEMP_DIR_PREFIX = "./temp/";
    public static final String STUDENT_CODE_SUBDIR = "/student-code";
    public static final String GENERATED_CODE_SUBDIR = "/generated-code";
    public static final String GENERATED_TESTS_SUBDIR = "/generated-tests";
    public static final long MAX_FILE_SIZE_MB = 10;
    
    // Priority Levels
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_LOW = "LOW";
    
    // Bob API
    public static final String BOB_MODE_ADVANCED = "advanced";
    public static final String BOB_REPO_CONTEXT = "true";
    public static final int BOB_API_TIMEOUT_MS = 30000; // 30 seconds
    
    // File Extensions
    public static final String JAVA_EXTENSION = ".java";
    public static final String PDF_EXTENSION = ".pdf";
    public static final String ZIP_EXTENSION = ".zip";
    public static final String CSV_EXTENSION = ".csv";
    public static final String MD_EXTENSION = ".md";
    
    // Error Messages
    public static final String ERROR_NO_ASSIGNMENT_TEXT = "Please provide assignment text or upload a PDF file.";
    public static final String ERROR_NO_COURSE_NAME = "Please provide a course name.";
    public static final String ERROR_NO_DUE_DATE = "Please provide a due date.";
    public static final String ERROR_PDF_EXTRACTION = "Failed to extract text from PDF";
    public static final String ERROR_FILE_TOO_LARGE = "File size exceeds maximum allowed (%dMB)";
    public static final String ERROR_INVALID_FILE_TYPE = "Invalid file type. Expected: %s";
    
    // Success Messages
    public static final String SUCCESS_ANALYSIS_COMPLETE = "Assignment analyzed successfully! IBM Bob has generated your personalized study plan.";
    
    // Logging
    public static final String LOG_ANALYSIS_START = "Starting assignment analysis for course: {}";
    public static final String LOG_ANALYSIS_COMPLETE = "Analysis completed in {}ms";
    public static final String LOG_FILE_UPLOAD = "Processing uploaded file: {} (size: {} bytes)";
    public static final String LOG_BOB_API_CALL = "Calling Bob API with prompt type: {}";
    
    // Study Schedule
    public static final int DEFAULT_DAILY_STUDY_HOURS = 2;
    public static final int MINUTES_PER_HOUR = 60;
    
    // HTTP Headers
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    // Content Types
    public static final String CONTENT_TYPE_CSV = "text/csv";
    public static final String CONTENT_TYPE_MARKDOWN = "text/markdown";
    public static final String CONTENT_TYPE_JSON = "application/json";
}

// Made with Bob
