#!/bin/bash
# Replit startup script for Assignment Decoder

echo "╔════════════════════════════════════════╗"
echo "║   📚 Assignment Decoder - Replit       ║"
echo "║   Building and starting application... ║"
echo "╚════════════════════════════════════════╝"

# Check if Maven wrapper exists, otherwise use system Maven
if [ -f "./apache-maven-3.9.15/bin/mvn" ]; then
    MAVEN_CMD="./apache-maven-3.9.15/bin/mvn"
else
    MAVEN_CMD="mvn"
fi

# Clean and build the project
echo "🔨 Building project with Maven..."
$MAVEN_CMD clean package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "🚀 Starting Spring Boot application..."
    java -jar target/assignment-decoder-1.0.0.jar
else
    echo "❌ Build failed. Please check the logs above."
    exit 1
fi

# Made with Bob
