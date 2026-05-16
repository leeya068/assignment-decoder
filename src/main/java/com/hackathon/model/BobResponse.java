package com.hackathon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BobResponse {
    
    // Core response fields
    private String response;
    private String status;
    private String sessionId;
    private long processingTimeMs;
    
    // Analysis-specific fields
    private List<TaskItem> tasks;
    private List<String> alreadyComplete;
    private List<String> ambiguousRequirements;
    private List<CodeSuggestion> codeSuggestions;
    
    // Code generation fields
    private List<CodeBlock> generatedCode;
    private List<TestCase> testCases;
    
    // Schedule fields
    private Schedule schedule;
    
    // Metadata
    private Metadata metadata;
    
    // ========== Constructors ==========
    
    public BobResponse() {
        this.tasks = new ArrayList<>();
        this.alreadyComplete = new ArrayList<>();
        this.ambiguousRequirements = new ArrayList<>();
        this.codeSuggestions = new ArrayList<>();
        this.generatedCode = new ArrayList<>();
        this.testCases = new ArrayList<>();
    }
    
    public BobResponse(String response, String status) {
        this();
        this.response = response;
        this.status = status;
    }
    
    // ========== Inner Class: TaskItem ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaskItem {
        private String description;
        private String priority; // HIGH, MEDIUM, LOW
        private int minutes;
        private String file;
        private String dependsOn;
        private boolean completed;
        
        // Constructors
        public TaskItem() {}
        
        public TaskItem(String description, String priority, int minutes) {
            this.description = description;
            this.priority = priority;
            this.minutes = minutes;
            this.completed = false;
        }
        
        // Getters and Setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public int getMinutes() { return minutes; }
        public void setMinutes(int minutes) { this.minutes = minutes; }
        
        public String getFile() { return file; }
        public void setFile(String file) { this.file = file; }
        
        public String getDependsOn() { return dependsOn; }
        public void setDependsOn(String dependsOn) { this.dependsOn = dependsOn; }
        
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s (%d min) - %s", 
                priority, description, minutes, 
                completed ? "✓ DONE" : "⏳ PENDING");
        }
    }
    
    // ========== Inner Class: CodeSuggestion ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodeSuggestion {
        private String function;
        private String reason;
        private String suggestedImplementation;
        private String fileName;
        private int lineNumber;
        private String complexity;
        
        // Constructors
        public CodeSuggestion() {}
        
        public CodeSuggestion(String function, String reason, String suggestedImplementation) {
            this.function = function;
            this.reason = reason;
            this.suggestedImplementation = suggestedImplementation;
        }
        
        // Getters and Setters
        public String getFunction() { return function; }
        public void setFunction(String function) { this.function = function; }
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        
        public String getSuggestedImplementation() { return suggestedImplementation; }
        public void setSuggestedImplementation(String suggestedImplementation) { 
            this.suggestedImplementation = suggestedImplementation; 
        }
        
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        
        public int getLineNumber() { return lineNumber; }
        public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
        
        public String getComplexity() { return complexity; }
        public void setComplexity(String complexity) { this.complexity = complexity; }
    }
    
    // ========== Inner Class: CodeBlock ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodeBlock {
        private String language;
        private String code;
        private String description;
        private int lineCount;
        private Map<String, String> placeholders; // TODO markers with hints
        
        // Constructors
        public CodeBlock() {}
        
        public CodeBlock(String language, String code, String description) {
            this.language = language;
            this.code = code;
            this.description = description;
            this.lineCount = code.split("\n").length;
        }
        
        // Getters and Setters
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        
        public String getCode() { return code; }
        public void setCode(String code) { 
            this.code = code;
            this.lineCount = code != null ? code.split("\n").length : 0;
        }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getLineCount() { return lineCount; }
        public void setLineCount(int lineCount) { this.lineCount = lineCount; }
        
        public Map<String, String> getPlaceholders() { return placeholders; }
        public void setPlaceholders(Map<String, String> placeholders) { this.placeholders = placeholders; }
    }
    
    // ========== Inner Class: TestCase ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestCase {
        private String testName;
        private String displayName;
        private String code;
        private List<String> assertions;
        private List<String> edgeCases;
        private boolean isParameterized;
        
        // Constructors
        public TestCase() {
            this.assertions = new ArrayList<>();
            this.edgeCases = new ArrayList<>();
        }
        
        public TestCase(String testName, String displayName, String code) {
            this();
            this.testName = testName;
            this.displayName = displayName;
            this.code = code;
        }
        
        // Getters and Setters
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public List<String> getAssertions() { return assertions; }
        public void setAssertions(List<String> assertions) { this.assertions = assertions; }
        
        public List<String> getEdgeCases() { return edgeCases; }
        public void setEdgeCases(List<String> edgeCases) { this.edgeCases = edgeCases; }
        
        public boolean isParameterized() { return isParameterized; }
        public void setParameterized(boolean parameterized) { isParameterized = parameterized; }
    }
    
    // ========== Inner Class: Schedule ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule {
        private List<DailyPlan> days;
        private int totalHours;
        private String recommendation;
        private List<String> criticalPath;
        
        // Constructors
        public Schedule() {
            this.days = new ArrayList<>();
            this.criticalPath = new ArrayList<>();
        }
        
        // Getters and Setters
        public List<DailyPlan> getDays() { return days; }
        public void setDays(List<DailyPlan> days) { this.days = days; }
        
        public int getTotalHours() { return totalHours; }
        public void setTotalHours(int totalHours) { this.totalHours = totalHours; }
        
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
        
        public List<String> getCriticalPath() { return criticalPath; }
        public void setCriticalPath(List<String> criticalPath) { this.criticalPath = criticalPath; }
    }
    
    // ========== Inner Class: DailyPlan ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyPlan {
        private int day;
        private String date;
        private List<String> tasks;
        private int estimatedMinutes;
        private boolean isRestDay;
        private String focus;
        
        // Constructors
        public DailyPlan() {
            this.tasks = new ArrayList<>();
        }
        
        public DailyPlan(int day, List<String> tasks, int estimatedMinutes) {
            this();
            this.day = day;
            this.tasks = tasks;
            this.estimatedMinutes = estimatedMinutes;
            this.isRestDay = false;
        }
        
        // Getters and Setters
        public int getDay() { return day; }
        public void setDay(int day) { this.day = day; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public List<String> getTasks() { return tasks; }
        public void setTasks(List<String> tasks) { this.tasks = tasks; }
        
        public int getEstimatedMinutes() { return estimatedMinutes; }
        public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
        
        public boolean isRestDay() { return isRestDay; }
        public void setRestDay(boolean restDay) { isRestDay = restDay; }
        
        public String getFocus() { return focus; }
        public void setFocus(String focus) { this.focus = focus; }
    }
    
    // ========== Inner Class: Metadata ==========
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private String modelVersion;
        private int tokensUsed;
        private List<String> filesAnalyzed;
        private String confidence;
        private List<String> warnings;
        
        // Constructors
        public Metadata() {
            this.filesAnalyzed = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }
        
        // Getters and Setters
        public String getModelVersion() { return modelVersion; }
        public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
        
        public int getTokensUsed() { return tokensUsed; }
        public void setTokensUsed(int tokensUsed) { this.tokensUsed = tokensUsed; }
        
        public List<String> getFilesAnalyzed() { return filesAnalyzed; }
        public void setFilesAnalyzed(List<String> filesAnalyzed) { this.filesAnalyzed = filesAnalyzed; }
        
        public String getConfidence() { return confidence; }
        public void setConfidence(String confidence) { this.confidence = confidence; }
        
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
    
    // ========== Main Class Getters and Setters ==========
    
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    
    public List<TaskItem> getTasks() { return tasks; }
    public void setTasks(List<TaskItem> tasks) { this.tasks = tasks; }
    
    public List<String> getAlreadyComplete() { return alreadyComplete; }
    public void setAlreadyComplete(List<String> alreadyComplete) { this.alreadyComplete = alreadyComplete; }
    
    public List<String> getAmbiguousRequirements() { return ambiguousRequirements; }
    public void setAmbiguousRequirements(List<String> ambiguousRequirements) { 
        this.ambiguousRequirements = ambiguousRequirements; 
    }
    
    public List<CodeSuggestion> getCodeSuggestions() { return codeSuggestions; }
    public void setCodeSuggestions(List<CodeSuggestion> codeSuggestions) { 
        this.codeSuggestions = codeSuggestions; 
    }
    
    public List<CodeBlock> getGeneratedCode() { return generatedCode; }
    public void setGeneratedCode(List<CodeBlock> generatedCode) { this.generatedCode = generatedCode; }
    
    public List<TestCase> getTestCases() { return testCases; }
    public void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }
    
    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
    
    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }
    
    // ========== Helper Methods ==========
    
    /**
     * Checks if Bob's response indicates success
     */
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status) || 
               "completed".equalsIgnoreCase(status);
    }
    
    /**
     * Gets total estimated minutes from all tasks
     */
    public int getTotalEstimatedMinutes() {
        if (tasks == null) return 0;
        return tasks.stream().mapToInt(TaskItem::getMinutes).sum();
    }
    
    /**
     * Gets total lines of generated code
     */
    public int getTotalGeneratedLines() {
        if (generatedCode == null) return 0;
        return generatedCode.stream().mapToInt(CodeBlock::getLineCount).sum();
    }
    
    /**
     * Returns formatted summary for evidence logging
     */
    public String toSummaryString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════╗\n");
        sb.append("║           IBM BOB RESPONSE SUMMARY             ║\n");
        sb.append("╚════════════════════════════════════════════════╝\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Session: ").append(sessionId).append("\n");
        sb.append("Processing Time: ").append(processingTimeMs).append("ms\n");
        
        if (tasks != null && !tasks.isEmpty()) {
            sb.append("\n📋 TASKS (").append(tasks.size()).append("):\n");
            for (TaskItem task : tasks) {
                sb.append("  • ").append(task.toString()).append("\n");
            }
            sb.append("  Total: ").append(getTotalEstimatedMinutes()).append(" minutes\n");
        }
        
        if (generatedCode != null && !generatedCode.isEmpty()) {
            sb.append("\n💻 GENERATED CODE (").append(generatedCode.size()).append(" blocks):\n");
            for (CodeBlock block : generatedCode) {
                sb.append("  • ").append(block.getDescription() != null ? block.getDescription() : "Code block")
                  .append(" (").append(block.getLineCount()).append(" lines, ")
                  .append(block.getLanguage()).append(")\n");
            }
        }
        
        if (testCases != null && !testCases.isEmpty()) {
            sb.append("\n🧪 TEST CASES (").append(testCases.size()).append("):\n");
            for (TestCase test : testCases) {
                sb.append("  • ").append(test.getDisplayName() != null ? test.getDisplayName() : test.getTestName())
                  .append("\n");
            }
        }
        
        if (metadata != null) {
            sb.append("\n📊 METADATA:\n");
            sb.append("  • Model: ").append(metadata.getModelVersion()).append("\n");
            sb.append("  • Tokens: ").append(metadata.getTokensUsed()).append("\n");
            sb.append("  • Confidence: ").append(metadata.getConfidence()).append("\n");
            if (metadata.getFilesAnalyzed() != null && !metadata.getFilesAnalyzed().isEmpty()) {
                sb.append("  • Files Analyzed: ").append(metadata.getFilesAnalyzed().size()).append("\n");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Creates a mock response for demo/testing without real API
     */
    public static BobResponse createMockAnalysisResponse() {
        BobResponse response = new BobResponse();
        response.setStatus("success");
        response.setSessionId("mock-session-" + System.currentTimeMillis());
        response.setProcessingTimeMs(2450);
        
        // Add mock tasks
        List<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem("Create User class with name, email, id fields", "HIGH", 15));
        tasks.add(new TaskItem("Implement UserService.saveUser() with validation", "HIGH", 30));
        tasks.add(new TaskItem("Add email regex validation", "MEDIUM", 10));
        tasks.add(new TaskItem("Write JUnit tests for UserService", "HIGH", 20));
        tasks.add(new TaskItem("Handle duplicate user ID exception", "LOW", 15));
        response.setTasks(tasks);
        
        // Add mock "already complete" list
        List<String> alreadyComplete = new ArrayList<>();
        alreadyComplete.add("User class constructor");
        alreadyComplete.add("Basic getter/setter methods");
        response.setAlreadyComplete(alreadyComplete);
        
        // Add ambiguous requirements
        List<String> ambiguous = new ArrayList<>();
        ambiguous.add("'robust solution' - clarify what constitutes robust");
        ambiguous.add("'efficient' - specify time/space complexity requirements");
        response.setAmbiguousRequirements(ambiguous);
        
        // Add metadata
        Metadata metadata = new Metadata();
        metadata.setModelVersion("bob-v2.5");
        metadata.setTokensUsed(1247);
        metadata.setConfidence("HIGH");
        response.setMetadata(metadata);
        
        return response;
    }
    
    public static BobResponse createMockCodeGenerationResponse() {
        BobResponse response = new BobResponse();
        response.setStatus("success");
        response.setProcessingTimeMs(1890);
        
        // Add mock generated code
        List<CodeBlock> codeBlocks = new ArrayList<>();
        
        CodeBlock insertMethod = new CodeBlock();
        insertMethod.setLanguage("java");
        insertMethod.setDescription("BST insert method with recursive helper");
        insertMethod.setCode("""
            /**
             * Inserts a value into the BST.
             * @param value the value to insert
             */
            public void insert(int value) {
                root = insertRecursive(root, value);
            }
            
            private Node insertRecursive(Node current, int value) {
                if (current == null) {
                    return new Node(value);
                }
                if (value < current.data) {
                    current.left = insertRecursive(current.left, value);
                } else if (value > current.data) {
                    current.right = insertRecursive(current.right, value);
                }
                return current;
            }
            """);
        codeBlocks.add(insertMethod);
        
        response.setGeneratedCode(codeBlocks);
        
        return response;
    }
    
    public static BobResponse createMockTestGenerationResponse() {
        BobResponse response = new BobResponse();
        response.setStatus("success");
        response.setProcessingTimeMs(1560);
        
        // Add mock test cases
        List<TestCase> tests = new ArrayList<>();
        
        TestCase test1 = new TestCase();
        test1.setTestName("testInsertIntoEmptyTree");
        test1.setDisplayName("Should insert into empty BST");
        test1.setCode("""
            @Test
            @DisplayName("Should insert into empty BST")
            void testInsertIntoEmptyTree() {
                BinarySearchTree bst = new BinarySearchTree();
                bst.insert(10);
                assertNotNull(bst.root);
                assertEquals(10, bst.root.data);
            }
            """);
        tests.add(test1);
        
        TestCase test2 = new TestCase();
        test2.setTestName("testInsertDuplicate");
        test2.setDisplayName("Should handle duplicate values appropriately");
        test2.setCode("""
            @Test
            @DisplayName("Should handle duplicate values")
            void testInsertDuplicate() {
                BinarySearchTree bst = new BinarySearchTree();
                bst.insert(10);
                bst.insert(10);
                // BST typically ignores duplicates or counts frequency
                assertNotNull(bst.search(10));
            }
            """);
        tests.add(test2);
        
        response.setTestCases(tests);
        
        return response;
    }
    
    @Override
    public String toString() {
        return toSummaryString();
    }
}