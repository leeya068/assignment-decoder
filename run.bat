@echo off
REM Assignment Decoder - Easy Run Script
REM This script makes it easier to run Maven commands

echo ╔════════════════════════════════════════════════╗
echo ║   Assignment Decoder with IBM Bob              ║
echo ║   Quick Start Script                           ║
echo ╚════════════════════════════════════════════════╝
echo.

set MAVEN_CMD=apache-maven-3.9.15\bin\mvn.cmd

if "%1"=="" goto menu
if "%1"=="run" goto run
if "%1"=="build" goto build
if "%1"=="clean" goto clean
if "%1"=="test" goto test
goto menu

:menu
echo Available commands:
echo   run.bat run     - Start the application
echo   run.bat build   - Build the project
echo   run.bat clean   - Clean build artifacts
echo   run.bat test    - Run tests
echo.
echo Or just run: run.bat (shows this menu)
goto end

:run
echo Starting Spring Boot application...
echo Application will be available at http://localhost:8080
echo Press Ctrl+C to stop
echo.
call %MAVEN_CMD% spring-boot:run
goto end

:build
echo Building project...
call %MAVEN_CMD% clean install
goto end

:clean
echo Cleaning project...
call %MAVEN_CMD% clean
goto end

:test
echo Running tests...
call %MAVEN_CMD% test
goto end

:end

@REM Made with Bob
