# 📊 Streamlit Demo - Assignment Decoder

A Python-based web interface for the Assignment Decoder application, built with Streamlit.

## 🎯 Overview

This Streamlit app provides an alternative frontend that communicates with the Java Spring Boot backend via REST API. It's perfect for:

- 🚀 Quick demos and presentations
- 📊 Data science-focused audiences
- 🐍 Python developers
- ☁️ Easy cloud deployment on Streamlit Cloud

## 🏗️ Architecture

```
┌─────────────────┐         HTTP/REST        ┌──────────────────┐
│  Streamlit App  │ ◄──────────────────────► │  Spring Boot     │
│  (Python)       │                           │  Backend (Java)  │
│  Port 8501      │                           │  Port 8080       │
└─────────────────┘                           └──────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- Python 3.8 or higher
- Java backend running (see main README)

### Option 1: Using the Run Script (Easiest)

**Windows:**
```bash
run.bat
```

**Linux/Mac:**
```bash
chmod +x run.sh
./run.sh
```

### Option 2: Manual Setup

```bash
# Create virtual environment
python -m venv venv

# Activate virtual environment
# Windows:
venv\Scripts\activate
# Linux/Mac:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Run Streamlit
streamlit run app.py
```

### Option 3: Direct Run (No Virtual Environment)

```bash
pip install -r requirements.txt
streamlit run app.py
```

## ⚙️ Configuration

### Local Development

Edit `.streamlit/secrets.toml`:

```toml
BACKEND_URL = "http://localhost:8080"
BOB_API_KEY = "your-api-key"
BOB_API_URL = "https://your-endpoint.com/api"
```

### Streamlit Cloud Deployment

1. Go to [Streamlit Cloud](https://streamlit.io/cloud)
2. Connect your GitHub repository
3. Set main file: `streamlit_demo/app.py`
4. Add secrets in Settings → Secrets:

```toml
BACKEND_URL = "https://your-backend-url.com"
BOB_API_KEY = "your-api-key"
BOB_API_URL = "https://your-endpoint.com/api"
```

## 📁 Project Structure

```
streamlit_demo/
├── app.py                      # Main Streamlit application
├── requirements.txt            # Python dependencies
├── run.bat                     # Windows startup script
├── run.sh                      # Linux/Mac startup script
├── .streamlit/
│   ├── config.toml            # Streamlit configuration
│   └── secrets.toml           # Local secrets (not committed)
└── README.md                  # This file
```

## ✨ Features

### User Interface
- 🌙 **Light/Dark Theme Toggle** - Switch between themes
- 📊 **Three-Tab Layout** - New Analysis, Results, Help
- 🎨 **Custom Styling** - Professional design with custom CSS
- 📱 **Responsive Design** - Works on all devices

### Functionality
- 📝 **Assignment Input** - Course name, description, due date
- ⏰ **Study Hours Slider** - Configure daily study time
- 📎 **File Upload** - Upload existing code (ZIP)
- 🚀 **One-Click Analysis** - Submit to backend
- 📊 **Results Display** - View tasks, code, tests, schedule
- 📋 **Copy Buttons** - Copy code/tests/schedule
- ✅ **Task Tracking** - Check off completed tasks

### Backend Integration
- 🔌 **Health Check** - Monitor backend status
- 🔄 **REST API Calls** - Communicate with Java backend
- ⚡ **Async Processing** - Non-blocking requests
- 🛡️ **Error Handling** - Graceful error messages

## 🎨 Customization

### Theme Colors

Edit `.streamlit/config.toml`:

```toml
[theme]
primaryColor = "#1f77b4"        # Blue
backgroundColor = "#ffffff"      # White
secondaryBackgroundColor = "#f0f2f6"  # Light gray
textColor = "#262730"           # Dark gray
font = "sans serif"
```

### Custom CSS

Modify the CSS in `app.py`:

```python
st.markdown("""
<style>
    .main-header {
        font-size: 3rem;
        color: #1f77b4;
    }
    /* Add your custom styles */
</style>
""", unsafe_allow_html=True)
```

## 🐛 Troubleshooting

### Backend Connection Failed

**Problem:** Cannot connect to backend

**Solutions:**
1. Ensure Java backend is running:
   ```bash
   cd ..
   run.bat run
   ```
2. Check `BACKEND_URL` in secrets
3. Verify backend health: `http://localhost:8080/actuator/health`

### Module Not Found

**Problem:** `ModuleNotFoundError: No module named 'streamlit'`

**Solution:**
```bash
pip install -r requirements.txt
```

### Port Already in Use

**Problem:** Port 8501 is already in use

**Solution:**
```bash
# Kill existing Streamlit process
# Windows:
taskkill /F /IM streamlit.exe

# Linux/Mac:
pkill -f streamlit

# Or use a different port:
streamlit run app.py --server.port 8502
```

### CORS Errors

**Problem:** CORS policy blocking requests

**Solution:** Add CORS configuration to Spring Boot backend:

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:8501", "https://*.streamlit.app")
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

## 📊 Deployment Options

### Streamlit Cloud (Recommended)

**Pros:**
- ✅ Free tier available
- ✅ Easy deployment from GitHub
- ✅ Automatic HTTPS
- ✅ Built-in secrets management

**Cons:**
- ❌ Requires backend deployed separately
- ❌ Limited resources on free tier

### Heroku

```bash
# Create Procfile
echo "web: streamlit run app.py --server.port=$PORT" > Procfile

# Deploy
heroku create your-app-name
git push heroku main
```

### Docker

```dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .

EXPOSE 8501

CMD ["streamlit", "run", "app.py", "--server.port=8501", "--server.address=0.0.0.0"]
```

## 🔒 Security

### Best Practices

1. **Never commit secrets** - Use `.streamlit/secrets.toml` (gitignored)
2. **Use environment variables** - For production deployments
3. **Enable HTTPS** - Always use SSL in production
4. **Validate inputs** - Backend handles validation
5. **Rate limiting** - Implement on backend

### Secrets Management

**Local Development:**
```toml
# .streamlit/secrets.toml (not committed)
BACKEND_URL = "http://localhost:8080"
BOB_API_KEY = "dev-key"
```

**Production:**
Use Streamlit Cloud secrets or environment variables

## 📈 Performance

### Optimization Tips

1. **Cache API responses:**
   ```python
   @st.cache_data(ttl=600)
   def fetch_analysis(assignment_text):
       # API call
   ```

2. **Use session state:**
   ```python
   if 'result' not in st.session_state:
       st.session_state['result'] = None
   ```

3. **Lazy loading:**
   - Load heavy components only when needed
   - Use tabs to organize content

## 🤝 Contributing

Contributions welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

MIT License - Same as main project

## 🙏 Acknowledgments

- **Streamlit Team** - For the amazing framework
- **Spring Boot** - For the robust backend
- **IBM Bob AI** - For the AI capabilities

## 📞 Support

- 🐛 **Issues:** [GitHub Issues](https://github.com/your-repo/issues)
- 💬 **Discussions:** [GitHub Discussions](https://github.com/your-repo/discussions)
- 📖 **Main Docs:** [../README.md](../README.md)
- 🚀 **Deployment:** [../DEPLOYMENT-GUIDE.md](../DEPLOYMENT-GUIDE.md)

---

**Made with ❤️ and 🐍 for IBM Hackathon 2026**