package com.hackathon.model;

import org.springframework.web.multipart.MultipartFile;

public class AssignmentRequest {
    private String assignmentText;
    private String courseName;
    private String dueDate;
    private MultipartFile assignmentPdf;
    private MultipartFile studentCodeZip;
    
    // Getters and Setters
    public String getAssignmentText() { return assignmentText; }
    public void setAssignmentText(String assignmentText) { this.assignmentText = assignmentText; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    
    public MultipartFile getAssignmentPdf() { return assignmentPdf; }
    public void setAssignmentPdf(MultipartFile assignmentPdf) { this.assignmentPdf = assignmentPdf; }
    
    public MultipartFile getStudentCodeZip() { return studentCodeZip; }
    public void setStudentCodeZip(MultipartFile studentCodeZip) { this.studentCodeZip = studentCodeZip; }
}