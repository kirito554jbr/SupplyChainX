# Quick Start Script for SupplyChainX
# This script will start the database and run the application

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "   SupplyChainX Application Startup Script    " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check if Docker is running
Write-Host "[1/5] Checking Docker..." -ForegroundColor Yellow
docker --version 2>&1 | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker is not running or not installed!" -ForegroundColor Red
    Write-Host "Please start Docker Desktop and try again." -ForegroundColor Red
    exit 1
}
Write-Host "✅ Docker is available" -ForegroundColor Green
Write-Host ""

# Step 2: Start the database
Write-Host "[2/5] Starting database..." -ForegroundColor Yellow
docker-compose up -d db phpmyadmin
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Failed to start database!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Database containers started" -ForegroundColor Green
Write-Host ""

# Step 3: Wait for database to be ready
Write-Host "[3/5] Waiting for database to be ready (15 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 15
Write-Host "✅ Database should be ready" -ForegroundColor Green
Write-Host ""

# Step 4: Compile the application
Write-Host "[4/5] Compiling application..." -ForegroundColor Yellow
.\mvnw.cmd clean compile -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Application compiled successfully" -ForegroundColor Green
Write-Host ""

# Step 5: Run the application
Write-Host "[5/5] Starting Spring Boot application..." -ForegroundColor Yellow
Write-Host "Watch for DataInitializer output below:" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

.\mvnw.cmd spring-boot:run

