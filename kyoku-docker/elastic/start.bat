@echo off
setlocal enabledelayedexpansion

echo ================================================================================================
echo       Elasticsearch Artist Search Setup
echo ================================================================================================

REM Load environment variables from .env file
pushd "%~dp0\.."

if not exist ".env" (
    echo [ERROR] .env file not found in root directory.
    
    exit /b 1
)

echo [INFO] Loading environment variables...
for /f "usebackq tokens=1,2 delims==" %%i in (".env") do (
    if not "%%i"=="" if not "%%i:~0,1%"=="#" (
        set "%%i=%%j"
    )
)

echo ----------------------------------------
echo   Step 1: Waiting for Elasticsearch
echo ----------------------------------------
echo [INFO] Waiting for Elasticsearch to be healthy...
:wait_es
docker-compose ps elasticsearch | findstr "healthy" >nul 2>&1
if !errorlevel! neq 0 (
    echo   ... still waiting for Elasticsearch ...
    timeout /t 5 /nobreak >nul
    goto wait_es
)
echo [SUCCESS] Elasticsearch is healthy!
echo.

echo ----------------------------------------
echo   Step 2: Setting up Kibana System User
echo ----------------------------------------
echo [INFO] Setting kibana_system user password...
curl -s -u elastic:%ELASTIC_ROOT_PASSWORD% -X POST http://localhost:1200/_security/user/kibana_system/_password -H "Content-Type: application/json" -d "{\"password\": \"%KIBANA_SYSTEM_PASSWORD%\"}" >nul 2>&1
if !errorlevel! neq 0 (
    echo [WARNING] Could not set kibana_system password. Kibana may fail to start.
) else (
    echo [SUCCESS] kibana_system password configured!
)
echo.

echo ----------------------------------------
echo   Step 3: Waiting for MySQL ProxySQL
echo ----------------------------------------
echo [INFO] Waiting for MySQL ProxySQL to be ready...

REM Wait for proxy to be ready
set "replica_attempts=0"
:wait_mysql_proxy
set /a replica_attempts+=1
docker exec content-primary mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% -hcontent-proxysql -P6033 -e "SELECT 1;" >nul 2>&1
if !errorlevel! equ 0 (
    echo [SUCCESS] content-proxysql is ready!
    goto :mysql_validation_success
)
if !replica_attempts! geq 10 (
    echo [ERROR] Could not connect to content-proxysql
    exit /b 1
)
echo [INFO] Waiting for content-proxysql... attempt !replica_attempts!/10
timeout /t 5 /nobreak >nul
goto :wait_mysql_proxy

:mysql_validation_success
echo ----------------------------------------
echo   Step 4: Building Elastic Setup Image  
echo ----------------------------------------
docker-compose build elastic-setup
if !errorlevel! neq 0 (
    echo [ERROR] Failed to build elastic-setup image!
    echo [INFO] Check docker-compose.yml and elastic/Dockerfile.
    
    exit /b 1
)
echo [SUCCESS] Elastic setup image built!
echo.

echo ----------------------------------------
echo   Step 5: Running Setup and Import
echo ----------------------------------------
echo [INFO] This will:
echo        1. Create the 'artists' index with mappings
echo        2. Import artist data from MySQL to Elasticsearch
echo.
docker-compose --profile setup run --rm elastic-setup

if !errorlevel! neq 0 (
    echo.
    echo [ERROR] Setup/Import failed with exit code !errorlevel!!
    echo [INFO] Troubleshooting:
    echo       1. Check logs: docker-compose logs elastic-setup
    echo       2. Verify Elasticsearch: docker-compose logs elasticsearch
    echo       3. Verify MySQL: docker-compose logs content-proxysql
    echo.
    
    exit /b 1
)

echo.
echo [SUCCESS] Setup and import completed successfully!
echo.

echo ----------------------------------------
echo   Step 6: Verifying Import
echo ----------------------------------------
echo [INFO] Checking document count in Elasticsearch...
curl -s -u elastic:%ELASTIC_ROOT_PASSWORD% http://localhost:1200/artists/_count > temp_count.json 2>nul
if !errorlevel! neq 0 (
    echo [WARNING] Could not query Elasticsearch for document count.
    goto :skip_verify
)

findstr "count" temp_count.json > temp_count_only.txt 2>nul
set /p COUNT_LINE=<temp_count_only.txt 2>nul
echo [INFO] Document count response: !COUNT_LINE!
del temp_count.json temp_count_only.txt 2>nul

:skip_verify
echo.
echo ================================================================================================
echo                          ELASTICSEARCH SETUP COMPLETE!
echo ================================================================================================
echo.
echo [INFO] Your Elasticsearch artist search is now configured.
echo [INFO] Next steps:
echo        - Access Kibana at: http://localhost:1201
echo        - Login with: elastic / %ELASTIC_ROOT_PASSWORD%
echo        - Explore the 'artists' index in Dev Tools
echo.
echo [INFO] Connection details:
echo   Elasticsearch:    http://localhost:1200
echo   Kibana:           http://localhost:1201
echo.
echo [INFO] Useful commands:
echo   - View ES logs:     docker-compose logs -f elasticsearch
echo   - Re-run import:   docker-compose --profile setup run --rm elastic-setup
echo.

