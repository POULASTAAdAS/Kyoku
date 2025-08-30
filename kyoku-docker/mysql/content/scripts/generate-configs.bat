@echo off
setlocal enabledelayedexpansion

echo ================================================
echo       Generating Configuration Files
echo ================================================
pushd "%~dp0..\..\.."
REM Check if .env file exists
if not exist ".env" (
    echo [ERROR] .env file not found in root directory.
    pause
    exit /b 1
)

echo [INFO] Loading environment variables from .env...
for /f "usebackq tokens=1,2 delims==" %%i in (".env") do (
    if not "%%i"=="" if not "%%i:~0,1%"=="#" (
        set "%%i=%%j"
    )
)

REM Check if template files exist
if not exist "mysql\content\proxy\proxysql.conf.template" (
    echo [ERROR] proxysql.conf.template not found!
    echo [INFO] Please create the template file first.
    pause
    exit /b 1
)

if not exist "mysql\content\sql\setup-replication-master.sql.template" (
    echo [ERROR] setup-replication-master.sql.template not found!
    echo [INFO] Please create the template file first.
    pause
    exit /b 1
)

REM Function to replace placeholders in file
echo [INFO] Generating proxysql.conf from template...
powershell -Command "(Get-Content 'mysql\content\proxy\proxysql.conf.template') -replace '{{MYSQL_CONTENT_PROXY_ADMIN}}', '%MYSQL_CONTENT_PROXY_ADMIN%' -replace '{{MYSQL_CONTENT_PROXY_PASSWORD}}', '%MYSQL_CONTENT_PROXY_PASSWORD%' -replace '{{MYSQL_CONTENT_USER}}', '%MYSQL_CONTENT_USER%' -replace '{{MYSQL_CONTENT_PASSWORD}}', '%MYSQL_CONTENT_PASSWORD%' -replace '{{MYSQL_CONTENT_MONITOR_USER}}', '%MYSQL_CONTENT_MONITOR_USER%' -replace '{{MYSQL_CONTENT_MONITOR_PASSWORD}}', '%MYSQL_CONTENT_MONITOR_PASSWORD%' | Set-Content 'mysql\content\proxy\proxysql.conf'"

echo [INFO] Generating setup-replication-master.sql from template...
powershell -Command "(Get-Content 'mysql\content\sql\setup-replication-master.sql.template') -replace '{{MYSQL_CONTENT_REPLICATION_USER}}', '%MYSQL_CONTENT_REPLICATION_USER%' -replace '{{MYSQL_CONTENT_REPLICATION_PASSWORD}}', '%MYSQL_CONTENT_REPLICATION_PASSWORD%' -replace '{{MYSQL_CONTENT_USER}}', '%MYSQL_CONTENT_USER%' -replace '{{MYSQL_CONTENT_PASSWORD}}', '%MYSQL_CONTENT_PASSWORD%' -replace '{{MYSQL_CONTENT_MONITOR_USER}}', '%MYSQL_CONTENT_MONITOR_USER%' -replace '{{MYSQL_CONTENT_MONITOR_PASSWORD}}', '%MYSQL_CONTENT_MONITOR_PASSWORD%' | Set-Content 'mysql\content\sql\setup-replication-master.sql'"

echo [SUCCESS] Configuration files generated successfully!
echo [INFO] Generated files:
echo   - mysql\content\proxy\proxysql.conf
echo   - mysql\content\sql\setup-replication-master.sql
echo.
REM Waiting 3 second....
echo [INFO] Waiting 3 second....
timeout /t 3 /nobreak >nul