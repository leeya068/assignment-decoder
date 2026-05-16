package com.hackathon.exception;

/**
 * Custom exception for assignment analysis errors.
 * Thrown when Bob API fails or analysis cannot be completed.
 */
public class AssignmentAnalysisException extends RuntimeException {
    
    private final String errorCode;
    
    public AssignmentAnalysisException(String message) {
        super(message);
        this.errorCode = "ANALYSIS_ERROR";
    }
    
    public AssignmentAnalysisException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ANALYSIS_ERROR";
    }
    
    public AssignmentAnalysisException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public AssignmentAnalysisException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}

// Made with Bob
