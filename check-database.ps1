# Check Database Content Script
# This script checks if the DataInitializer populated the database

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "   Database Content Checker                   " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Check if database container is running
Write-Host "Checking database container..." -ForegroundColor Yellow
$dbContainer = docker ps --filter "name=db" --format "{{.Names}}"
if ($dbContainer -ne "db") {
    Write-Host "❌ Database container is not running!" -ForegroundColor Red
    Write-Host "Start it with: docker-compose up -d db" -ForegroundColor Yellow
    exit 1
}
Write-Host "✅ Database container is running" -ForegroundColor Green
Write-Host ""

# Check if supplyChainX database exists
Write-Host "Checking database existence..." -ForegroundColor Yellow
$databases = docker exec db mysql -uroot -proot -e "SHOW DATABASES LIKE 'supplyChainX';" 2>&1
if ($databases -match "supplyChainX") {
    Write-Host "✅ Database 'supplyChainX' exists" -ForegroundColor Green
} else {
    Write-Host "❌ Database 'supplyChainX' does not exist!" -ForegroundColor Red
    Write-Host "The application needs to run first to create it." -ForegroundColor Yellow
    exit 1
}
Write-Host ""

# Check if users table exists
Write-Host "Checking users table..." -ForegroundColor Yellow
$tables = docker exec db mysql -uroot -proot -e "USE supplyChainX; SHOW TABLES LIKE 'users';" 2>&1
if ($tables -match "users") {
    Write-Host "✅ Table 'users' exists" -ForegroundColor Green
} else {
    Write-Host "❌ Table 'users' does not exist!" -ForegroundColor Red
    Write-Host "The application needs to run first to create it." -ForegroundColor Yellow
    exit 1
}
Write-Host ""

# Check user count
Write-Host "Checking user count..." -ForegroundColor Yellow
$userCount = docker exec db mysql -uroot -proot -e "USE supplyChainX; SELECT COUNT(*) as count FROM users;" 2>&1 | Select-String -Pattern "^\d+$"
Write-Host ""

# List all users
Write-Host "Current users in database:" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
docker exec db mysql -uroot -proot -e "USE supplyChainX; SELECT idUser, email, firstName, lastName, role, enabled FROM users;" 2>&1

Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "phpMyAdmin is available at: http://localhost:8089" -ForegroundColor Green
Write-Host "Login: root / root" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Cyan

