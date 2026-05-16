package com.hackathon.exception;

/**
 * Exception thrown when file processing fails (PDF extraction, ZIP handling, etc.)
 */
public class FileProcessingException extends RuntimeException {
    
    private final String fileName;
    
    public FileProcessingException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }
    
    public FileProcessingException(String message, String fileName, Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return fileName;
    }
}

// Made with Bob
