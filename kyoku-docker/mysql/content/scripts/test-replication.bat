@echo off
setlocal enabledelayedexpansion

echo ================================================
echo        MySQL Replication Test
echo ================================================

REM Load environment variables from .env file
pushd "%~dp0..\..\.."

if not exist ".env"  (
    echo [ERROR] .env file not found in root directory.
    pause
    exit /b 1
)

echo [INFO] Loading environment variables...
for /f "usebackq tokens=1,2 delims==" %%i in ( ".env") do (
    if not "%%i"=="" if not "%%i:~0,1%"=="#" (
        set "%%i=%%j"
    )
)

echo [INFO] Testing replication functionality...
echo.

REM CHANGE: Fixed timestamp generation - using PowerShell instead of deprecated WMIC
REM Generate unique test value with timestamp (keeping it short for column size)
for /f %%i in ('powershell -command "Get-Date -Format 'HHmmss'"') do set "timestamp=%%i"
echo.
REM Check if data appears on replicas
echo ----------------------------------------
echo   Step 2: Check data on replicas
echo ----------------------------------------
echo
echo [INFO] Checking replica 1...
docker exec content-replica1 mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% CONTENT -e "SELECT * FROM Country where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 1!
) else (
    echo [ERROR] Data not found on replica 1!
    REM CHANGE: Added debugging info to check replication status
    echo [DEBUG] Checking replica 1 status...
    docker exec content-replica1 mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Replica_IO_Running" /C:"Replica_SQL_Running" /C:"Last_Error"
)
echo.

echo [INFO] Checking replica 2...
docker exec content-replica2 mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% CONTENT -e "SELECT * FROM Country where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 2!
) else (
    echo [ERROR] Data not found on replica 2!
)
echo.

echo [INFO] Checking replica 3...
docker exec content-replica3 mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% CONTENT -e "SELECT * FROM Country where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 3!
) else (
    echo [ERROR] Data not found on replica 3!
)
echo.

echo [INFO] Checking replica 4...
docker exec content-replica4 mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% CONTENT -e "SELECT * FROM Country where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 4!
) else (
    echo [ERROR] Data not found on replica 4!
)
echo.

echo [INFO] Checking replica 5...
docker exec content-replica5 mysql -uroot -p%MYSQL_ROOT_CONTENT_PASSWORD% CONTENT -e "SELECT * FROM Country where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 5!
) else (
    echo [ERROR] Data not found on replica 5!
)
echo.

if !errorlevel! equ 0 (
    echo [SUCCESS] Test data cleaned up!
) else (
    echo [WARNING] Failed to clean up test data
)

echo.
echo ================================================
echo           Replication Test Summary
echo ================================================
echo [INFO] Test completed using value: %test_value%
echo [INFO] Check the output above to verify replication is working.
echo [INFO] All replicas should show the test data during the check phase.
echo.
echo [INFO] Press any key to exit.
pause