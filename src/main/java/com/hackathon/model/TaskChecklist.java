package com.hackathon.model;

import java.util.ArrayList;
import java.util.List;

public class TaskChecklist {
    private String assignmentName;
    private List<Task> tasks = new ArrayList<>();
    private List<String> generatedCodeStubs = new ArrayList<>();
    private List<String> generatedTests = new ArrayList<>();
    private String estimatedTimeHours;
    
    public static class Task {
        private String description;
        private String priority; // HIGH, MEDIUM, LOW
        private String estimatedMinutes;
        private String relatedFile;
        private boolean completed;
        
        // Constructors
        public Task() {}
        
        public Task(String description, String priority, String estimatedMinutes) {
            this.description = description;
            this.priority = priority;
            this.estimatedMinutes = estimatedMinutes;
            this.completed = false;
        }
        
        // Getters and Setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public String getEstimatedMinutes() { return estimatedMinutes; }
        public void setEstimatedMinutes(String estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
        
        public String getRelatedFile() { return relatedFile; }
        public void setRelatedFile(String relatedFile) { this.relatedFile = relatedFile; }
        
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }
    
    // Getters and Setters
    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    
    public List<String> getGeneratedCodeStubs() { return generatedCodeStubs; }
    public void setGeneratedCodeStubs(List<String> generatedCodeStubs) { this.generatedCodeStubs = generatedCodeStubs; }
    
    public List<String> getGeneratedTests() { return generatedTests; }
    public void setGeneratedTests(List<String> generatedTests) { this.generatedTests = generatedTests; }
    
    public String getEstimatedTimeHours() { return estimatedTimeHours; }
    public void setEstimatedTimeHours(String estimatedTimeHours) { this.estimatedTimeHours = estimatedTimeHours; }
}