@echo off
REM Quick start script for Streamlit demo (Windows)

echo ╔════════════════════════════════════════╗
echo ║   📚 Assignment Decoder - Streamlit    ║
echo ║   Starting demo application...         ║
echo ╚════════════════════════════════════════╝

REM Check if Python is installed
python --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Python is not installed. Please install Python 3.8 or higher.
    exit /b 1
)

REM Check if virtual environment exists
if not exist "venv" (
    echo 📦 Creating virtual environment...
    python -m venv venv
)

REM Activate virtual environment
echo 🔧 Activating virtual environment...
call venv\Scripts\activate.bat

REM Install dependencies
echo 📥 Installing dependencies...
pip install -r requirements.txt

REM Check if backend is running
echo 🔍 Checking backend connection...
set BACKEND_URL=http://localhost:8080
curl -s "%BACKEND_URL%/actuator/health" >nul 2>&1
if errorlevel 1 (
    echo ⚠️  Warning: Backend not detected at %BACKEND_URL%
    echo    Make sure to start the Java backend first!
    echo    Run: cd .. ^&^& run.bat run
) else (
    echo ✅ Backend is running at %BACKEND_URL%
)

REM Start Streamlit
echo.
echo 🚀 Starting Streamlit application...
echo    Access at: http://localhost:8501
echo.
streamlit run app.py

@REM Made with Bob
