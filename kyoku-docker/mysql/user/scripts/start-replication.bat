@echo off
setlocal enabledelayedexpansion

echo ================================================
echo       MySQL Replication Complete Setup
echo ================================================

REM Check if Docker is running
docker --version >nul 2>&1
if !errorlevel! neq 0 (
    echo [ERROR] Docker is not running or not installed!
    echo [INFO] Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo [INFO] Docker is running!
echo.

REM Generate configuration files from templates
REM  ----------------------------------------
REM    Step 0: Generate configuration files
REM  ----------------------------------------
call mysql\user\scripts\generate-configs.bat

if !errorlevel! neq 0 (
    echo [ERROR] Failed to generate configuration files!
    pause
    exit /b 1
)

echo.

REM Start Docker services
echo ----------------------------------------
echo   Step 1: Starting Docker services
echo ----------------------------------------
echo [INFO] Starting all services with docker-compose...
docker-compose up -d

if !errorlevel! neq 0 (
    echo [ERROR] Failed to start Docker services!
    echo [INFO] Check your docker-compose.yml file and try again.
    pause
    exit /b 1
)

echo [SUCCESS] Docker services started!
echo.

REM Wait for services to initialize
echo [INFO] Waiting 30 seconds for all services to initialize...
echo [INFO] This includes MySQL initialization and ProxySQL setup...
timeout /t 30 /nobreak >nul

REM Run replication setup
echo ----------------------------------------
echo   Step 2: Setting up replication
echo ----------------------------------------
call mysql\user\scripts\setup-replication.bat

echo.
echo ----------------------------------------
echo   Step 3: Testing replication
echo ----------------------------------------
echo [INFO] Running replication test...
call mysql\user\scripts\test-replication.bat

echo.
echo ================================================
echo              Setup Complete!
echo ================================================
echo [INFO] Your MySQL Master-Slave replication is now configured.
echo [INFO] You can use the following scripts for monitoring:
echo.
echo   mysql\user\scripts\check-replication.bat  - Check replication status
echo   mysql\user\scripts\test-replication.bat   - Test replication functionality
echo   generate-configs.bat                       - Regenerate config files
echo.
echo [INFO] Connection details:
echo   Primary (Master): localhost:1000
echo   Replica 1:        localhost:1001  
echo   Replica 2:        localhost:1002
echo   Replica 3:        localhost:1003
echo   ProxySQL:         localhost:1004 (MySQL) / localhost:1005 (Admin)
echo.
echo [INFO] Press any key to exit.
pause