#!/bin/bash
# Quick start script for Streamlit demo

echo "╔════════════════════════════════════════╗"
echo "║   📚 Assignment Decoder - Streamlit    ║"
echo "║   Starting demo application...         ║"
echo "╚════════════════════════════════════════╝"

# Check if Python is installed
if ! command -v python3 &> /dev/null; then
    echo "❌ Python 3 is not installed. Please install Python 3.8 or higher."
    exit 1
fi

# Check if virtual environment exists
if [ ! -d "venv" ]; then
    echo "📦 Creating virtual environment..."
    python3 -m venv venv
fi

# Activate virtual environment
echo "🔧 Activating virtual environment..."
source venv/bin/activate

# Install dependencies
echo "📥 Installing dependencies..."
pip install -r requirements.txt

# Check if backend is running
echo "🔍 Checking backend connection..."
BACKEND_URL=${BACKEND_URL:-"http://localhost:8080"}
if curl -s "$BACKEND_URL/actuator/health" > /dev/null 2>&1; then
    echo "✅ Backend is running at $BACKEND_URL"
else
    echo "⚠️  Warning: Backend not detected at $BACKEND_URL"
    echo "   Make sure to start the Java backend first!"
    echo "   Run: cd .. && run.bat run"
fi

# Start Streamlit
echo ""
echo "🚀 Starting Streamlit application..."
echo "   Access at: http://localhost:8501"
echo ""
streamlit run app.py

# Made with Bob
