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
if not exist "mysql\user\proxy\proxysql.cnf.template" (
    echo [ERROR] proxysql.cnf.template not found!
    echo [INFO] Please create the template file first.
    pause
    exit /b 1
)

if not exist "mysql\user\sql\setup-replication-master.sql.template" (
    echo [ERROR] setup-replication-master.sql.template not found!
    echo [INFO] Please create the template file first.
    pause
    exit /b 1
)

REM Function to replace placeholders in file
echo [INFO] Generating proxysql.cnf from template...
powershell -Command "(Get-Content 'mysql\user\proxy\proxysql.cnf.template') -replace '{{MYSQL_USER_PROXY_ADMIN}}', '%MYSQL_USER_PROXY_ADMIN%' -replace '{{MYSQL_USER_PROXY_PASSWORD}}', '%MYSQL_USER_PROXY_PASSWORD%' -replace '{{MYSQL_USER_USER}}', '%MYSQL_USER_USER%' -replace '{{MYSQL_ROOT_USER_PASSWORD}}', '%MYSQL_ROOT_USER_PASSWORD%' -replace '{{MYSQL_USER_MONITOR_USER}}', '%MYSQL_USER_MONITOR_USER%' -replace '{{MYSQL_USER_MONITOR_PASSWORD}}', '%MYSQL_USER_MONITOR_PASSWORD%' | Set-Content 'mysql\user\proxy\proxysql.cnf'"

echo [INFO] Generating setup-replication-master.sql from template...
powershell -Command "(Get-Content 'mysql\user\sql\setup-replication-master.sql.template') -replace '{{MYSQL_USER_REPLICATION_USER}}', '%MYSQL_USER_REPLICATION_USER%' -replace '{{MYSQL_USER_REPLICATION_PASSWORD}}', '%MYSQL_USER_REPLICATION_PASSWORD%' -replace '{{MYSQL_USER_USER}}', '%MYSQL_USER_USER%' -replace '{{MYSQL_ROOT_USER_PASSWORD}}', '%MYSQL_ROOT_USER_PASSWORD%' -replace '{{MYSQL_USER_MONITOR_USER}}', '%MYSQL_USER_MONITOR_USER%' -replace '{{MYSQL_USER_MONITOR_PASSWORD}}', '%MYSQL_USER_MONITOR_PASSWORD%' | Set-Content 'mysql\user\sql\setup-replication-master.sql'"

echo [SUCCESS] Configuration files generated successfully!
echo [INFO] Generated files:
echo   - mysql\user\proxy\proxysql.cnf
echo   - mysql\user\sql\setup-replication-master.sql
echo.
REM Waiting 3 second....
echo [INFO] Waiting 3 second....
timeout /t 3 /nobreak >nul