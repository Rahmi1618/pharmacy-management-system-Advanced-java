@echo off
echo ========================================
echo PharmaCare - Pharmacy Management System
echo ========================================
echo.

if not exist "bin\Main.class" (
    echo Error: Application not compiled!
    echo Please run compile.bat first.
    echo.
    pause
    exit /b 1
)

echo Starting PharmaCare application...
echo.

java -cp "bin;lib\*" Main

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo Application failed to start!
    echo ========================================
    echo.
    echo Please check:
    echo 1. MAMP MySQL server is running on port 3307
    echo 2. Database 'pharmacare' exists
    echo 3. All required JAR files are in lib folder
    echo.
    pause
)
