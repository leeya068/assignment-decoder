# 🚀 Assignment Decoder - Complete Setup Guide

## Prerequisites Installation

### 1. Install Java JDK 17+
**Download:** https://adoptium.net/temurin/releases/?version=17
- Choose: Windows x64 JDK .msi installer
- Run installer and follow prompts
- Verify: Open new PowerShell and run `java -version`

### 2. Install Apache Maven
**Download:** https://maven.apache.org/download.cgi
- Download: apache-maven-3.9.6-bin.zip

**Installation Steps:**
1. Extract to: `C:\Program Files\Apache\maven\apache-maven-3.9.6`
2. Add Environment Variables:
   - Open: System Properties → Environment Variables
   - New System Variable:
     - Name: `MAVEN_HOME`
     - Value: `C:\Program Files\Apache\maven\apache-maven-3.9.6`
   - Edit `Path` variable, add new entry:
     - `%MAVEN_HOME%\bin`
3. **IMPORTANT:** Close and reopen PowerShell/VS Code
4. Verify: `mvn -version`

## Running the Application

### Step 1: Build the Project
```powershell
mvn clean install
```

### Step 2: Run the Application
```powershell
mvn spring-boot:run
```

### Step 3: Access the Application
Open browser: http://localhost:8080

## Project Structure
```
assignment-decoder/
├── src/main/java/com/hackathon/
│   ├── AssignmentDecoderApplication.java  # Main entry point
│   ├── controller/
│   │   └── DecoderController.java         # Web endpoints
│   ├── service/
│   │   ├── BobAnalysisService.java        # IBM Bob integration
│   │   └── CodeGeneratorService.java      # Code generation
│   └── model/
│       ├── AssignmentRequest.java         # Input model
│       ├── TaskChecklist.java             # Task breakdown
│       └── BobResponse.java               # Bob's responses
├── src/main/resources/
│   ├── application.properties             # Configuration
│   └── templates/                         # HTML pages
│       ├── index.html                     # Main form
│       ├── results.html                   # Analysis results
│       └── evidence.html                  # Bob interaction logs
└── evidence/
    └── bob-prompts.md                     # Evidence documentation

```

## Testing the Application

### Test 1: Basic Assignment Analysis
1. Go to http://localhost:8080
2. Fill in:
   - Course Name: "CS-101: Data Structures"
   - Due Date: (any future date)
   - Assignment Text: "Build a binary search tree with insert, delete, and search methods"
3. Click "Ask IBM Bob to Decode This Assignment"
4. View the generated checklist and schedule

### Test 2: View Evidence
1. Click "View IBM Bob Interaction Evidence" link
2. See all Bob interactions logged

## Configuration

### IBM Bob API Setup (For Production)
Edit `src/main/resources/application.properties`:
```properties
bob.api.url=https://your-actual-ibm-bob-endpoint.com/api
bob.api.key=your-actual-api-key
```

Currently using mock responses for demo purposes.

## Troubleshooting

### Maven not found
- Ensure MAVEN_HOME is set correctly
- Ensure %MAVEN_HOME%\bin is in PATH
- **Close and reopen PowerShell/VS Code**

### Java version error
- Need Java 17 or higher
- Check: `java -version`

### Port 8080 already in use
- Change port in application.properties:
  ```properties
  server.port=8081
  ```

### Build errors
```powershell
# Clean and rebuild
mvn clean install -U
```

## Next Steps for Hackathon

1. ✅ Code structure complete
2. ⏳ Install Maven and test locally
3. ⏳ Replace mock Bob responses with real API
4. ⏳ Add screenshots to evidence folder
5. ⏳ Create demo video
6. ⏳ Prepare submission package

## Features Demonstrated

- 📋 **Task Breakdown**: Bob analyzes vague assignments into actionable tasks
- 💻 **Code Generation**: Bob creates method stubs with JavaDoc
- 🧪 **Test Writing**: Bob generates JUnit test cases
- 📅 **Study Planning**: Bob creates day-by-day schedules
- 🔍 **Code Analysis**: Bob compares existing code vs requirements

## Evidence Collection

All Bob interactions are logged to:
- Console output
- `evidence/bob-logs.txt`
- Visible at http://localhost:8080/evidence

Take screenshots of:
1. Assignment input form
2. Generated task checklist
3. Code stubs generated
4. Test cases generated
5. Study schedule
6. Evidence page showing Bob prompts

---

**Built for IBM Hackathon 2026 - Student Productivity Category**