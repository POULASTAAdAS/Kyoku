@echo off
setlocal enabledelayedexpansion

echo ================================================================================================
echo       Development Environment Setup Script Runnig......
echo ================================================================================================

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
echo  ================================================================================================
echo    Step 1: Generate configuration files
echo  ================================================================================================
call mysql\user\scripts\generate-configs.bat
if !errorlevel! neq 0 (
    echo [ERROR] Failed to generate mysql\user\scripts\generate-configs.bat configuration files!
    pause
    exit /b 1
)

call mysql\playlist\scripts\generate-configs.bat
if !errorlevel! neq 0 (
    echo [ERROR] Failed to generate mysql\playlist\scripts\generate-configs.bat configuration files!
    pause
    exit /b 1
)

call mysql\activity\scripts\generate-configs.bat
if !errorlevel! neq 0 (
    echo [ERROR] Failed to generate mysql\activity\scripts\generate-configs.bat configuration files!
    pause
    exit /b 1
)


REM Start Docker services
echo ================================================================================================
echo   Step 2: Starting Docker services
echo ================================================================================================
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

echo
echo ================================================================================================
echo                                            1
echo ================================================================================================
call mysql\user\scripts\start-replication.bat
echo ================================================================================================
echo                                            2
echo ================================================================================================
call mysql\playlist\scripts\start-replication.bat
echo ================================================================================================
echo                                            3
echo ================================================================================================
call mysql\activity\scripts\start-replication.bat
echo ================================================================================================
echo ==============================================DONE==============================================
echo ==============================================DONE==============================================
echo ==============================================DONE==============================================
echo ==============================================DONE==============================================
echo ==============================================DONE==============================================
echo ==============================================DONE==============================================