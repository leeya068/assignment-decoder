package com.hackathon.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request model for assignment analysis.
 * Contains assignment details and optional file uploads.
 */
public class AssignmentRequest {
    
    @Size(max = 10000, message = "Assignment text must not exceed 10000 characters")
    private String assignmentText;
    
    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String courseName;
    
    @NotBlank(message = "Due date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Due date must be in format YYYY-MM-DD")
    private String dueDate;
    
    private MultipartFile assignmentPdf;
    private MultipartFile studentCodeZip;
    
    // Constructors
    public AssignmentRequest() {}
    
    public AssignmentRequest(String assignmentText, String courseName, String dueDate) {
        this.assignmentText = assignmentText;
        this.courseName = courseName;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public String getAssignmentText() {
        return assignmentText;
    }
    
    public void setAssignmentText(String assignmentText) {
        this.assignmentText = assignmentText;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public MultipartFile getAssignmentPdf() {
        return assignmentPdf;
    }
    
    public void setAssignmentPdf(MultipartFile assignmentPdf) {
        this.assignmentPdf = assignmentPdf;
    }
    
    public MultipartFile getStudentCodeZip() {
        return studentCodeZip;
    }
    
    public void setStudentCodeZip(MultipartFile studentCodeZip) {
        this.studentCodeZip = studentCodeZip;
    }
    
    /**
     * Checks if either assignment text or PDF is provided.
     */
    public boolean hasAssignmentContent() {
        return (assignmentText != null && !assignmentText.trim().isEmpty()) ||
               (assignmentPdf != null && !assignmentPdf.isEmpty());
    }
    
    @Override
    public String toString() {
        return String.format("AssignmentRequest{courseName='%s', dueDate='%s', hasText=%b, hasPdf=%b, hasCode=%b}",
            courseName, dueDate,
            assignmentText != null && !assignmentText.isEmpty(),
            assignmentPdf != null && !assignmentPdf.isEmpty(),
            studentCodeZip != null && !studentCodeZip.isEmpty());
    }
}