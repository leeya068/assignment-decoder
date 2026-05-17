# 🚀 Quick Deployment Guide

Choose your deployment platform and get started in minutes!

---

## 🎯 Which Platform Should I Choose?

| Platform | Best For | Difficulty | Cost | Setup Time |
|----------|----------|------------|------|------------|
| **Replit** | Hackathon demos, quick sharing | ⭐ Easy | Free-$20/mo | 5 min |
| **Docker** | Production, cloud platforms | ⭐⭐ Medium | $10-50/mo | 15 min |
| **Streamlit** | Python demos, data science | ⭐ Easy | Free-$20/mo | 10 min |
| **Vercel** | Frontend hosting (experimental) | ⭐⭐⭐ Hard | Free-$20/mo | 20 min |

---

## 🟢 Option 1: Replit (Recommended for Hackathons)

**Perfect for:** Quick demos, sharing with judges, collaborative development

### Steps:

1. **Go to [Replit.com](https://replit.com)** and sign in
2. **Click "Create Repl" → "Import from GitHub"**
3. **Enter your repository URL**
4. **Add secrets** (click 🔒 icon):
   ```
   BOB_API_KEY=your-api-key
   BOB_API_URL=https://your-endpoint.com/api
   ```
5. **Click "Run"** - That's it! ✅

**Access:** Your app will be available at `https://your-repl-name.your-username.repl.co`

**Pros:**
- ✅ Zero configuration needed
- ✅ Instant sharing with URL
- ✅ Built-in IDE for live editing
- ✅ Free tier available

**Cons:**
- ❌ Limited resources on free tier
- ❌ May sleep after inactivity

---

## 🐳 Option 2: Docker (Recommended for Production)

**Perfect for:** Production deployments, AWS/GCP/Azure, scalability

### Quick Start:

```bash
# 1. Build and run with Docker Compose
docker-compose up -d

# 2. Access at http://localhost:8080
```

### Deploy to Cloud:

**AWS (ECS/Fargate):**
```bash
# Build and push
docker build -t assignment-decoder .
docker tag assignment-decoder:latest <account>.dkr.ecr.us-east-1.amazonaws.com/assignment-decoder
docker push <account>.dkr.ecr.us-east-1.amazonaws.com/assignment-decoder
```

**Google Cloud Run:**
```bash
gcloud builds submit --tag gcr.io/PROJECT-ID/assignment-decoder
gcloud run deploy --image gcr.io/PROJECT-ID/assignment-decoder --platform managed
```

**Azure:**
```bash
az container create --resource-group myGroup --name assignment-decoder \
  --image assignment-decoder:latest --dns-name-label assignment-decoder --ports 8080
```

**Pros:**
- ✅ Production-ready
- ✅ Scalable
- ✅ Works on any cloud platform
- ✅ Consistent environment

**Cons:**
- ❌ Requires Docker knowledge
- ❌ More setup required
- ❌ Costs money on cloud platforms

---

## 🐍 Option 3: Streamlit Cloud (Python Frontend)

**Perfect for:** Python developers, data science presentations, quick demos

### Architecture:
```
Streamlit Frontend (Python) → Spring Boot Backend (Java)
```

### Steps:

**1. Deploy Backend First** (use Replit or Docker)

**2. Deploy Streamlit Frontend:**
   - Go to [Streamlit Cloud](https://streamlit.io/cloud)
   - Click "New app"
   - Connect GitHub
   - Set main file: `streamlit_demo/app.py`
   - Add secrets:
     ```toml
     BACKEND_URL = "https://your-backend-url.com"
     ```
   - Click "Deploy"

**3. Access:** Your app at `https://your-app.streamlit.app`

### Local Testing:

```bash
cd streamlit_demo
pip install -r requirements.txt
streamlit run app.py
```

**Pros:**
- ✅ Beautiful Python UI
- ✅ Easy deployment
- ✅ Free tier available
- ✅ Great for demos

**Cons:**
- ❌ Requires backend deployed separately
- ❌ Two-tier architecture

---

## ⚡ Option 4: Vercel (Experimental)

**Perfect for:** Frontend hosting, serverless functions

**Note:** Vercel has limited Java support. Best used for frontend only.

### Steps:

```bash
# 1. Install Vercel CLI
npm install -g vercel

# 2. Deploy
vercel --prod

# 3. Set environment variables
vercel env add BOB_API_KEY
vercel env add BOB_API_URL
```

**Pros:**
- ✅ Fast CDN
- ✅ Automatic HTTPS
- ✅ Free tier

**Cons:**
- ❌ Limited Java support
- ❌ 10-second timeout
- ❌ Better for frontend only

---

## 🔑 Environment Variables

All platforms need these:

```bash
BOB_API_KEY=your-ibm-bob-api-key
BOB_API_URL=https://your-ibm-bob-endpoint.com/api
```

### How to Set:

- **Replit:** Secrets tab (🔒 icon)
- **Docker:** `.env` file or `docker-compose.yml`
- **Streamlit:** Settings → Secrets
- **Vercel:** `vercel env add`

---

## ✅ Deployment Checklist

Before going live:

- [ ] Test locally with `run.bat run`
- [ ] Set all environment variables
- [ ] Test Bob API connection
- [ ] Verify file upload works
- [ ] Test on mobile devices
- [ ] Enable HTTPS/SSL
- [ ] Set up monitoring
- [ ] Document the URL
- [ ] Test with sample assignments

---

## 🆘 Quick Troubleshooting

### Backend Won't Start
```bash
# Check port 8080
netstat -ano | findstr :8080

# Kill process
taskkill /PID <process_id> /F
```

### Can't Connect to Bob API
```bash
# Test API
curl -H "Authorization: Bearer $BOB_API_KEY" $BOB_API_URL/health
```

### Docker Build Fails
```bash
# Clear cache
docker system prune -a

# Rebuild
docker-compose build --no-cache
```

---

## 📊 Cost Comparison

### Free Tier Options:
- **Replit Free:** ✅ Limited resources, public repls
- **Streamlit Free:** ✅ 1 app, public
- **Vercel Hobby:** ✅ Personal projects
- **Docker Local:** ✅ Free on your machine

### Paid Options:
- **Replit Hacker:** $7/month - Always-on, private
- **Streamlit Community:** $20/month - 3 apps
- **AWS/GCP/Azure:** $10-50/month - Production-ready
- **Vercel Pro:** $20/month - Commercial use

---

## 🎯 Recommended Setup for Hackathon

**For Demo Day:**
1. **Deploy backend on Replit** (5 minutes)
2. **Deploy frontend on Streamlit Cloud** (5 minutes)
3. **Share both URLs with judges** ✅

**Total Time:** 10 minutes
**Total Cost:** $0 (free tiers)

---

## 📚 Full Documentation

For detailed instructions, see:
- 📖 [DEPLOYMENT-GUIDE.md](DEPLOYMENT-GUIDE.md) - Complete deployment guide
- 📖 [README.md](README.md) - Main documentation
- 📖 [streamlit_demo/README.md](streamlit_demo/README.md) - Streamlit guide

---

## 🚀 One-Command Deployments

### Replit
```bash
# Just click "Run" button in Replit!
```

### Docker
```bash
docker-compose up -d
```

### Streamlit
```bash
cd streamlit_demo && streamlit run app.py
```

### Local Development
```bash
run.bat run
```

---

## 💡 Pro Tips

1. **Use Replit for hackathons** - Fastest setup, easy sharing
2. **Use Docker for production** - Most reliable, scalable
3. **Use Streamlit for Python audiences** - Beautiful UI, easy deployment
4. **Test locally first** - Always test before deploying
5. **Keep secrets secret** - Never commit API keys
6. **Monitor your app** - Use health checks and logging
7. **Have a backup plan** - Deploy to multiple platforms

---

## 🎉 Success!

Once deployed, you should see:

```
╔════════════════════════════════════════╗
║   📚 Assignment Decoder with IBM Bob   ║
║   http://your-deployment-url           ║
╚════════════════════════════════════════╝
```

**Share your URL and start helping students! 🎓**

---

**Need Help?**
- 🐛 [Report Issues](https://github.com/your-repo/issues)
- 💬 [Ask Questions](https://github.com/your-repo/discussions)
- 📖 [Read Full Guide](DEPLOYMENT-GUIDE.md)

**Made with ❤️ for IBM Hackathon 2026**