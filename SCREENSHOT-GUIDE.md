# 📸 Screenshot Guide for Assignment Decoder

This guide provides multiple methods to capture screenshots of your application for documentation and hackathon submission.

---

## Method 1: Automated Python Script (Recommended)

### Prerequisites
```bash
# Install required packages
pip install selenium pillow

# Download ChromeDriver
# Visit: https://chromedriver.chromium.org/downloads
# Download version matching your Chrome browser
# Extract chromedriver.exe to your project folder or add to PATH
```

### Usage
```bash
# Make sure application is running at http://localhost:8080
python take-screenshots.py
```

### What It Does
1. Opens Chrome browser automatically
2. Navigates to your application
3. Fills in sample assignment data
4. Submits the form
5. Captures screenshots of:
   - Home page
   - Filled form
   - Results page (multiple sections)
   - Evidence page
6. Saves all screenshots to `evidence/screenshots/`

---

## Method 2: Manual Screenshots (Simple)

### Windows Built-in Tools

#### Option A: Snipping Tool
1. Press `Windows + Shift + S`
2. Select area to capture
3. Screenshot copied to clipboard
4. Paste into Paint or Word
5. Save to `evidence/screenshots/`

#### Option B: Full Screen
1. Press `PrtScn` (Print Screen)
2. Open Paint
3. Paste (Ctrl+V)
4. Save to `evidence/screenshots/`

#### Option C: Active Window
1. Press `Alt + PrtScn`
2. Captures only active window
3. Paste and save

### Recommended Screenshots to Take

1. **Home Page** (`01_home_page.png`)
   - URL: http://localhost:8080
   - Show empty form

2. **Form Filled** (`02_form_filled.png`)
   - Fill in:
     - Course: "CS-101 Introduction to Programming"
     - Assignment: "Build a REST API with CRUD operations..."
     - Due Date: Future date

3. **Results Overview** (`03_results_overview.png`)
   - After submitting form
   - Show task breakdown

4. **Task Checklist** (`04_task_checklist.png`)
   - Scroll to tasks section
   - Show priorities and time estimates

5. **Generated Code** (`05_generated_code.png`)
   - Scroll to code section
   - Show Bob's generated code stubs

6. **Test Cases** (`06_test_cases.png`)
   - Show generated JUnit tests

7. **Study Schedule** (`07_study_schedule.png`)
   - Show day-by-day plan

8. **Evidence Page** (`08_evidence_page.png`)
   - URL: http://localhost:8080/evidence
   - Show Bob interaction logs

---

## Method 3: Browser Developer Tools

### Chrome/Edge
1. Press `F12` to open DevTools
2. Press `Ctrl + Shift + P`
3. Type "screenshot"
4. Choose:
   - "Capture full size screenshot" (entire page)
   - "Capture screenshot" (visible area)
   - "Capture node screenshot" (specific element)

### Firefox
1. Press `F12`
2. Click camera icon in toolbar
3. Choose "Save full page" or "Save visible"

---

## Method 4: PowerShell Script (Windows)

Create a file `take-screenshot.ps1`:

```powershell
# Simple PowerShell screenshot script
Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

function Take-Screenshot {
    param(
        [string]$Path = "evidence/screenshots/screenshot_$(Get-Date -Format 'yyyyMMdd_HHmmss').png"
    )
    
    # Create directory if it doesn't exist
    $dir = Split-Path $Path
    if (!(Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
    
    # Get screen bounds
    $bounds = [System.Windows.Forms.Screen]::PrimaryScreen.Bounds
    
    # Create bitmap
    $bitmap = New-Object System.Drawing.Bitmap $bounds.Width, $bounds.Height
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    # Capture screen
    $graphics.CopyFromScreen($bounds.Location, [System.Drawing.Point]::Empty, $bounds.Size)
    
    # Save
    $bitmap.Save($Path)
    
    # Cleanup
    $graphics.Dispose()
    $bitmap.Dispose()
    
    Write-Host "✅ Screenshot saved: $Path" -ForegroundColor Green
}

# Usage
Write-Host "📸 Screenshot Tool" -ForegroundColor Cyan
Write-Host "Press any key to take a screenshot..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
Take-Screenshot
```

Run with:
```powershell
powershell -ExecutionPolicy Bypass -File take-screenshot.ps1
```

---

## Method 5: Third-Party Tools

### Recommended Free Tools

1. **ShareX** (Best for Windows)
   - Download: https://getsharex.com/
   - Features: Scrolling capture, annotations, auto-upload
   - Hotkey: Ctrl+PrtScn

2. **Greenshot** (Simple & Effective)
   - Download: https://getgreenshot.org/
   - Features: Region capture, annotations
   - Hotkey: PrtScn

3. **Lightshot** (Quick & Easy)
   - Download: https://app.prntscr.com/
   - Features: Quick upload, editing
   - Hotkey: PrtScn

---

## Screenshot Checklist for Hackathon

### Essential Screenshots (Minimum 8)
- [ ] 1. Home page with empty form
- [ ] 2. Form filled with sample assignment
- [ ] 3. Results page - task breakdown
- [ ] 4. Results page - generated code
- [ ] 5. Results page - test cases
- [ ] 6. Results page - study schedule
- [ ] 7. Evidence page - Bob logs
- [ ] 8. Browser console showing Bob interactions

### Bonus Screenshots (Impressive)
- [ ] Application startup in terminal
- [ ] Project structure in VS Code
- [ ] Running tests
- [ ] Code quality (clean code examples)
- [ ] Error handling (validation messages)
- [ ] Mobile responsive view

---

## Tips for Great Screenshots

### 1. **Clean Your Screen**
- Close unnecessary tabs
- Hide personal information
- Use incognito/private mode if needed

### 2. **Good Lighting**
- Use light theme for better visibility
- Ensure text is readable
- Avoid glare on screen

### 3. **Highlight Important Parts**
- Use arrows or boxes to point out features
- Add text annotations
- Circle key elements

### 4. **Consistent Sizing**
- Use same resolution for all screenshots
- Recommended: 1920x1080 or 1280x720
- Crop to remove unnecessary borders

### 5. **File Naming**
- Use descriptive names
- Include numbers for ordering
- Format: `01_home_page.png`, `02_form_filled.png`

### 6. **Organize Files**
```
evidence/
└── screenshots/
    ├── 01_home_page.png
    ├── 02_form_filled.png
    ├── 03_results_overview.png
    ├── 04_task_checklist.png
    ├── 05_generated_code.png
    ├── 06_test_cases.png
    ├── 07_study_schedule.png
    └── 08_evidence_page.png
```

---

## Quick Start (Easiest Method)

### For Non-Technical Users:
1. Open application: http://localhost:8080
2. Press `Windows + Shift + S`
3. Drag to select area
4. Screenshot auto-copied to clipboard
5. Open Paint, paste, save to `evidence/screenshots/`
6. Repeat for each page

### For Technical Users:
1. Install: `pip install selenium pillow`
2. Download ChromeDriver
3. Run: `python take-screenshots.py`
4. Done! All screenshots auto-captured

---

## Troubleshooting

### Python Script Issues
**Error: "chromedriver not found"**
- Download from https://chromedriver.chromium.org/
- Place in project folder or add to PATH

**Error: "selenium not installed"**
- Run: `pip install selenium pillow`

**Error: "Connection refused"**
- Make sure app is running: `run.bat run`
- Check URL: http://localhost:8080

### Screenshot Quality Issues
**Blurry screenshots**
- Use higher resolution
- Don't zoom browser in/out
- Capture at 100% zoom level

**Missing content**
- Use "full page screenshot" option
- Or take multiple screenshots while scrolling

---

## For Hackathon Judges

Include these screenshots in your submission:

1. **README.md** - Embed key screenshots
2. **Presentation Slides** - Use for demo
3. **Evidence Folder** - Complete collection
4. **Video Demo** - Screen recording (optional)

### Creating a Video Demo
```bash
# Windows: Use Xbox Game Bar
Win + G -> Record

# Or use OBS Studio (free)
# Download: https://obsproject.com/
```

---

## Need Help?

If you encounter issues:
1. Check that application is running
2. Verify browser is up to date
3. Try manual screenshot method first
4. Check `evidence/screenshots/` folder exists

**Happy screenshotting! 📸**