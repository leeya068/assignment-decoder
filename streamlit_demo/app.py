"""
Streamlit Demo Wrapper for Assignment Decoder
This provides a Python-based UI that calls the Java Spring Boot backend
"""

import streamlit as st
import requests
import json
from datetime import datetime, timedelta

# Configure Streamlit page
st.set_page_config(
    page_title="📚 Assignment Decoder with IBM Bob",
    page_icon="📚",
    layout="wide",
    initial_sidebar_state="expanded"
)

# Backend URL (adjust based on deployment)
BACKEND_URL = st.secrets.get("BACKEND_URL", "http://localhost:8080")

# Custom CSS
st.markdown("""
<style>
    .main-header {
        font-size: 3rem;
        color: #1f77b4;
        text-align: center;
        margin-bottom: 2rem;
    }
    .stButton>button {
        width: 100%;
        background-color: #1f77b4;
        color: white;
        font-size: 1.2rem;
        padding: 0.75rem;
        border-radius: 0.5rem;
    }
    .success-box {
        padding: 1rem;
        background-color: #d4edda;
        border-left: 5px solid #28a745;
        border-radius: 0.5rem;
        margin: 1rem 0;
    }
    .code-block {
        background-color: #f8f9fa;
        padding: 1rem;
        border-radius: 0.5rem;
        border-left: 3px solid #1f77b4;
    }
</style>
""", unsafe_allow_html=True)

# Header
st.markdown('<h1 class="main-header">📚 Assignment Decoder with IBM Bob</h1>', unsafe_allow_html=True)
st.markdown("### Transform vague programming assignments into actionable study plans with AI-powered assistance")

# Sidebar
with st.sidebar:
    st.header("⚙️ Configuration")
    
    # Theme toggle
    theme = st.radio("🎨 Theme", ["Light", "Dark"], index=0)
    
    st.divider()
    
    st.header("📊 About")
    st.info("""
    **Assignment Decoder** uses IBM Bob AI to:
    - 📋 Decode vague requirements
    - 💻 Generate starter code
    - 🧪 Create test cases
    - 📅 Build study schedules
    - 🔍 Analyze existing code
    """)
    
    st.divider()
    
    # Backend status
    st.header("🔌 Backend Status")
    try:
        response = requests.get(f"{BACKEND_URL}/actuator/health", timeout=5)
        if response.status_code == 200:
            st.success("✅ Connected")
        else:
            st.error("❌ Backend unavailable")
    except:
        st.warning("⚠️ Backend not responding")

# Main content
tab1, tab2, tab3 = st.tabs(["📝 New Analysis", "📊 Results", "📖 Help"])

with tab1:
    st.header("Enter Assignment Details")
    
    col1, col2 = st.columns([2, 1])
    
    with col1:
        course_name = st.text_input(
            "📚 Course Name",
            placeholder="e.g., CS-101 Introduction to Programming",
            help="Enter the course code and name"
        )
        
        assignment_text = st.text_area(
            "📄 Assignment Description",
            height=200,
            placeholder="Paste your assignment description here...",
            help="Enter the full assignment text"
        )
    
    with col2:
        due_date = st.date_input(
            "📅 Due Date",
            value=datetime.now() + timedelta(days=7),
            help="When is the assignment due?"
        )
        
        daily_hours = st.slider(
            "⏰ Daily Study Hours",
            min_value=1,
            max_value=8,
            value=2,
            help="How many hours can you study per day?"
        )
        
        uploaded_file = st.file_uploader(
            "📎 Upload Existing Code (ZIP)",
            type=['zip'],
            help="Optional: Upload your existing code for analysis"
        )
    
    st.divider()
    
    if st.button("🚀 Ask IBM Bob to Decode", type="primary"):
        if not course_name or not assignment_text:
            st.error("❌ Please fill in course name and assignment description")
        else:
            with st.spinner("🤖 Bob is analyzing your assignment..."):
                try:
                    # Prepare request
                    data = {
                        "courseName": course_name,
                        "assignmentText": assignment_text,
                        "dueDate": due_date.isoformat(),
                        "dailyStudyHours": daily_hours
                    }
                    
                    files = {}
                    if uploaded_file:
                        files['codeFile'] = uploaded_file
                    
                    # Call backend
                    response = requests.post(
                        f"{BACKEND_URL}/api/decode",
                        data=data,
                        files=files,
                        timeout=60
                    )
                    
                    if response.status_code == 200:
                        result = response.json()
                        st.session_state['analysis_result'] = result
                        st.success("✅ Analysis complete! Check the Results tab.")
                        st.balloons()
                    else:
                        st.error(f"❌ Error: {response.text}")
                        
                except Exception as e:
                    st.error(f"❌ Failed to connect to backend: {str(e)}")

with tab2:
    st.header("Analysis Results")
    
    if 'analysis_result' in st.session_state:
        result = st.session_state['analysis_result']
        
        # Task Checklist
        st.subheader("✅ Task Checklist")
        if 'checklist' in result:
            checklist = result['checklist']
            
            # Summary metrics
            col1, col2, col3, col4 = st.columns(4)
            with col1:
                st.metric("Total Tasks", checklist.get('totalTasks', 0))
            with col2:
                st.metric("High Priority", checklist.get('highPriorityCount', 0))
            with col3:
                st.metric("Medium Priority", checklist.get('mediumPriorityCount', 0))
            with col4:
                st.metric("Low Priority", checklist.get('lowPriorityCount', 0))
            
            st.divider()
            
            # Task list
            for task in checklist.get('tasks', []):
                with st.expander(f"{task.get('priority', 'MEDIUM')} - {task.get('title', 'Task')}"):
                    st.write(f"**Description:** {task.get('description', 'N/A')}")
                    st.write(f"**File:** `{task.get('fileName', 'N/A')}`")
                    st.write(f"**Estimated Time:** {task.get('estimatedTime', 'N/A')}")
                    
                    if st.checkbox(f"Mark as complete", key=f"task_{task.get('id')}"):
                        st.success("✅ Task completed!")
        
        st.divider()
        
        # Generated Code
        st.subheader("💻 Generated Code")
        if 'generatedCode' in result:
            st.code(result['generatedCode'], language='java')
            if st.button("📋 Copy Code"):
                st.success("Code copied to clipboard!")
        
        st.divider()
        
        # Test Cases
        st.subheader("🧪 Test Cases")
        if 'testCases' in result:
            st.code(result['testCases'], language='java')
            if st.button("📋 Copy Tests"):
                st.success("Tests copied to clipboard!")
        
        st.divider()
        
        # Study Schedule
        st.subheader("📅 Study Schedule")
        if 'schedule' in result:
            st.markdown(result['schedule'])
            if st.button("📋 Copy Schedule"):
                st.success("Schedule copied to clipboard!")
        
        # Download options
        st.divider()
        st.subheader("📥 Download Results")
        
        col1, col2, col3 = st.columns(3)
        with col1:
            if st.button("📄 Download as PDF"):
                st.info("PDF generation coming soon!")
        with col2:
            if st.button("📊 Download as CSV"):
                st.info("CSV export coming soon!")
        with col3:
            if st.button("📝 Download as Markdown"):
                st.info("Markdown export coming soon!")
    else:
        st.info("👈 Submit an assignment in the 'New Analysis' tab to see results here")

with tab3:
    st.header("📖 How to Use")
    
    st.markdown("""
    ### Getting Started
    
    1. **Enter Assignment Details**
       - Fill in your course name
       - Paste the assignment description
       - Set the due date
       - Choose your daily study hours
    
    2. **Optional: Upload Code**
       - If you have existing code, upload it as a ZIP file
       - Bob will analyze it and identify gaps
    
    3. **Click "Ask IBM Bob to Decode"**
       - Bob will analyze your assignment
       - Results appear in seconds
    
    4. **Review Results**
       - Check the task checklist
       - Copy generated code and tests
       - Follow the study schedule
    
    ### Features
    
    - ✅ **Smart Task Breakdown** - 15+ specific tasks with priorities
    - 💻 **Code Generation** - Fully documented starter code
    - 🧪 **Test Suite** - JUnit 5 tests with edge cases
    - 📅 **Study Schedule** - Day-by-day plan
    - 🔍 **Code Analysis** - Compare existing code vs requirements
    
    ### Tips
    
    - Be specific in your assignment description
    - Upload existing code for better analysis
    - Adjust daily study hours realistically
    - Check off tasks as you complete them
    
    ### Support
    
    - 🐛 Found a bug? Report it on GitHub
    - 💬 Have questions? Check the documentation
    - ✨ Feature request? Submit an idea
    """)

# Footer
st.divider()
st.markdown("""
<div style='text-align: center; color: #666;'>
    <p>Made with ❤️ for IBM Hackathon 2026 | Powered by IBM Bob AI</p>
    <p>⭐ Star us on GitHub | 📖 Read the docs | 🐛 Report issues</p>
</div>
""", unsafe_allow_html=True)

# Made with Bob
