# 🚀 Deployment Guide - Assignment Decoder

This guide covers deploying the Assignment Decoder application on three different platforms: **Replit**, **Vercel**, and **Streamlit Cloud**.

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Option 1: Replit (Recommended for Java)](#option-1-replit-recommended-for-java)
3. [Option 2: Docker Deployment](#option-2-docker-deployment)
4. [Option 3: Streamlit Cloud (Python Wrapper)](#option-3-streamlit-cloud-python-wrapper)
5. [Option 4: Vercel (Experimental)](#option-4-vercel-experimental)
6. [Environment Variables](#environment-variables)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

Before deploying, ensure you have:

- ✅ IBM Bob API credentials (API key and endpoint URL)
- ✅ Git installed (for version control)
- ✅ GitHub account (for Replit/Vercel integration)
- ✅ Java 17+ installed (for local testing)
- ✅ Maven 3.9+ (included in project)

---

## Option 1: Replit (Recommended for Java)

**Best for:** Quick demos, hackathon presentations, collaborative development

### Step 1: Import to Replit

1. Go to [Replit.com](https://replit.com) and sign in
2. Click **"Create Repl"**
3. Select **"Import from GitHub"**
4. Enter your repository URL
5. Replit will auto-detect the `.replit` configuration

### Step 2: Configure Environment Variables

In the Replit **Secrets** tab (🔒 icon), add:

```
BOB_API_KEY=your-actual-api-key-here
BOB_API_URL=https://your-ibm-bob-endpoint.com/api
```

### Step 3: Run the Application

1. Click the **"Run"** button
2. Replit will execute `run-replit.sh`
3. Maven will build the project
4. Spring Boot will start on port 8080
5. Access via the Replit webview URL

### Step 4: Deploy to Production

1. Click **"Deploy"** in Replit
2. Choose **"Autoscale"** deployment
3. Configure custom domain (optional)
4. Click **"Deploy"**

### Replit Configuration Files

- `.replit` - Main configuration
- `replit.nix` - System dependencies
- `run-replit.sh` - Startup script

---

## Option 2: Docker Deployment

**Best for:** Production deployments, cloud platforms (AWS, GCP, Azure)

### Step 1: Build Docker Image

```bash
# Build the image
docker build -t assignment-decoder:latest .

# Or use Docker Compose
docker-compose build
```

### Step 2: Run with Docker

```bash
# Run single container
docker run -d \
  -p 8080:8080 \
  -e BOB_API_KEY=your-api-key \
  -e BOB_API_URL=https://your-endpoint.com/api \
  --name assignment-decoder \
  assignment-decoder:latest

# Or use Docker Compose
docker-compose up -d
```

### Step 3: Access the Application

Open browser to: `http://localhost:8080`

### Step 4: Deploy to Cloud

#### AWS ECS/Fargate
```bash
# Tag and push to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com
docker tag assignment-decoder:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/assignment-decoder:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/assignment-decoder:latest
```

#### Google Cloud Run
```bash
# Build and deploy
gcloud builds submit --tag gcr.io/PROJECT-ID/assignment-decoder
gcloud run deploy assignment-decoder --image gcr.io/PROJECT-ID/assignment-decoder --platform managed
```

#### Azure Container Instances
```bash
# Create container instance
az container create \
  --resource-group myResourceGroup \
  --name assignment-decoder \
  --image assignment-decoder:latest \
  --dns-name-label assignment-decoder \
  --ports 8080
```

### Docker Configuration Files

- `Dockerfile` - Multi-stage build configuration
- `docker-compose.yml` - Orchestration configuration
- `.dockerignore` - Files to exclude from build

---

## Option 3: Streamlit Cloud (Python Wrapper)

**Best for:** Quick Python-based demos, data science presentations

### Architecture

The Streamlit app (`streamlit_demo/app.py`) acts as a frontend that calls your Java backend via REST API.

### Step 1: Deploy Java Backend First

Deploy the Spring Boot backend using one of the methods above (Replit, Docker, etc.)

### Step 2: Deploy Streamlit Frontend

1. Go to [Streamlit Cloud](https://streamlit.io/cloud)
2. Click **"New app"**
3. Connect your GitHub repository
4. Set main file path: `streamlit_demo/app.py`
5. Set Python version: 3.11

### Step 3: Configure Secrets

In Streamlit Cloud settings, add secrets:

```toml
BACKEND_URL = "https://your-backend-url.com"
BOB_API_KEY = "your-api-key"
BOB_API_URL = "https://your-endpoint.com/api"
```

### Step 4: Deploy

Click **"Deploy"** - Streamlit will:
1. Install dependencies from `requirements.txt`
2. Start the app on port 8501
3. Provide a public URL

### Local Testing

```bash
cd streamlit_demo

# Install dependencies
pip install -r requirements.txt

# Run Streamlit
streamlit run app.py
```

### Streamlit Configuration Files

- `streamlit_demo/app.py` - Main Streamlit application
- `streamlit_demo/requirements.txt` - Python dependencies
- `streamlit_demo/.streamlit/config.toml` - Theme configuration
- `streamlit_demo/.streamlit/secrets.toml` - Local secrets (not committed)

---

## Option 4: Vercel (Experimental)

**Note:** Vercel has limited Java support. This is experimental.

### Step 1: Install Vercel CLI

```bash
npm install -g vercel
```

### Step 2: Configure Project

The `vercel.json` file is already configured.

### Step 3: Deploy

```bash
# Login to Vercel
vercel login

# Deploy
vercel --prod
```

### Step 4: Set Environment Variables

```bash
vercel env add BOB_API_KEY
vercel env add BOB_API_URL
```

### Limitations

- Vercel serverless functions have 10-second timeout
- Java cold starts can be slow
- Consider using Vercel for frontend only, with backend elsewhere

---

## Environment Variables

All deployment methods require these environment variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `BOB_API_KEY` | IBM Bob API authentication key | `sk-abc123...` |
| `BOB_API_URL` | IBM Bob API endpoint URL | `https://api.ibm.com/bob` |
| `SERVER_PORT` | Application port (optional) | `8080` |
| `SPRING_PROFILES_ACTIVE` | Spring profile (optional) | `production` |

### Setting Environment Variables

#### Replit
Use the **Secrets** tab (🔒 icon)

#### Docker
Use `-e` flag or `docker-compose.yml` env section

#### Streamlit Cloud
Use **Settings → Secrets** in dashboard

#### Vercel
Use `vercel env add` command

---

## Deployment Checklist

Before deploying to production:

- [ ] Test locally with `run.bat run`
- [ ] Run all tests with `run.bat test`
- [ ] Set all required environment variables
- [ ] Configure custom domain (optional)
- [ ] Enable HTTPS/SSL
- [ ] Set up monitoring/logging
- [ ] Configure backup strategy
- [ ] Test file upload functionality
- [ ] Verify Bob API connectivity
- [ ] Check CORS settings for frontend
- [ ] Review security settings
- [ ] Set up CI/CD pipeline (optional)

---

## Troubleshooting

### Application Won't Start

**Problem:** Port 8080 already in use

**Solution:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <process_id> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Bob API Connection Failed

**Problem:** Cannot connect to IBM Bob API

**Solutions:**
1. Verify `BOB_API_KEY` is set correctly
2. Check `BOB_API_URL` is accessible
3. Test API with curl:
   ```bash
   curl -H "Authorization: Bearer $BOB_API_KEY" $BOB_API_URL/health
   ```
4. Check firewall/network settings

### Docker Build Fails

**Problem:** Maven dependencies not downloading

**Solution:**
```bash
# Clear Docker cache
docker system prune -a

# Rebuild without cache
docker build --no-cache -t assignment-decoder:latest .
```

### Streamlit Can't Connect to Backend

**Problem:** CORS errors or connection refused

**Solutions:**
1. Ensure backend is running and accessible
2. Check `BACKEND_URL` in Streamlit secrets
3. Add CORS configuration to Spring Boot:
   ```java
   @CrossOrigin(origins = "https://your-streamlit-app.streamlit.app")
   ```

### Replit Build Timeout

**Problem:** Maven build takes too long

**Solution:**
1. Use Replit's "Always On" feature
2. Increase timeout in `.replit`:
   ```toml
   [deployment]
   buildCommand = "timeout 600 bash run-replit.sh"
   ```

---

## Performance Optimization

### For Production Deployments

1. **Enable Caching**
   ```properties
   spring.cache.type=caffeine
   spring.cache.caffeine.spec=maximumSize=500,expireAfterWrite=10m
   ```

2. **Configure Thread Pool**
   ```properties
   spring.task.execution.pool.core-size=4
   spring.task.execution.pool.max-size=8
   ```

3. **Enable Compression**
   ```properties
   server.compression.enabled=true
   server.compression.mime-types=text/html,text/xml,text/plain,text/css,application/json
   ```

4. **Set JVM Options**
   ```bash
   JAVA_OPTS="-Xmx1024m -Xms512m -XX:+UseG1GC"
   ```

---

## Monitoring & Logging

### Enable Spring Boot Actuator

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Health Check Endpoints

- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info

### Log Aggregation

For production, consider:
- **ELK Stack** (Elasticsearch, Logstash, Kibana)
- **Splunk**
- **Datadog**
- **New Relic**

---

## Security Best Practices

1. **Never commit secrets** - Use environment variables
2. **Enable HTTPS** - Use SSL/TLS certificates
3. **Implement rate limiting** - Prevent API abuse
4. **Validate all inputs** - Already implemented with Bean Validation
5. **Keep dependencies updated** - Run `mvn versions:display-dependency-updates`
6. **Use security headers** - Add Spring Security if needed
7. **Monitor for vulnerabilities** - Use Snyk or Dependabot

---

## Cost Estimates

### Replit
- **Free Tier:** Limited resources, public repls
- **Hacker Plan:** $7/month - Always-on, private repls
- **Pro Plan:** $20/month - More resources, custom domains

### Docker on Cloud
- **AWS ECS Fargate:** ~$30-50/month (0.25 vCPU, 0.5 GB)
- **Google Cloud Run:** Pay per request, ~$10-30/month
- **Azure Container Instances:** ~$25-40/month

### Streamlit Cloud
- **Free Tier:** 1 app, public
- **Community Plan:** $20/month - 3 apps, private
- **Teams Plan:** $250/month - Unlimited apps

### Vercel
- **Hobby:** Free - Personal projects
- **Pro:** $20/month - Commercial use
- **Enterprise:** Custom pricing

---

## Support & Resources

- 📖 **Documentation:** [README.md](README.md)
- 🔧 **Setup Guide:** [SETUP-GUIDE.md](SETUP-GUIDE.md)
- 🐛 **Issues:** [GitHub Issues](https://github.com/your-repo/issues)
- 💬 **Discussions:** [GitHub Discussions](https://github.com/your-repo/discussions)
- 📧 **Email:** support@your-domain.com

---

## Quick Reference

### Replit
```bash
# Just click "Run" button
# Or use Shell:
bash run-replit.sh
```

### Docker
```bash
docker-compose up -d
```

### Streamlit
```bash
cd streamlit_demo
streamlit run app.py
```

### Local Development
```bash
run.bat run
```

---

**Made with ❤️ for IBM Hackathon 2026**

For more information, see [README.md](README.md)