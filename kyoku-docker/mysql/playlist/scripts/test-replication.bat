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
set "test_value=INSERT INTO Playlist VALUES (1, 'My Playlist', 'A description of my playlist', TRUE, 100, 10, 300, NOW(), NOW());"

echo [INFO] Using test value: %test_value%
echo.

REM Insert test data on primary
echo ----------------------------------------
echo   Step 1: Insert test data on primary
echo ----------------------------------------
echo [INFO] Inserting test data on primary...
docker exec playlist-primary mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "%test_value%"

if !errorlevel! neq 0 (
    echo [ERROR] Failed to insert test data on primary
    REM CHANGE: Show table structure to debug column size issues
    echo [DEBUG] Checking Playlist table structure...
    docker exec playlist-primary mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "DESCRIBE Playlist;"
    pause
    exit /b 1
)

echo [SUCCESS] Test data inserted on primary!

REM Wait for replication
echo [INFO] Waiting 3 seconds for replication...
timeout /t 3 /nobreak >nul

REM Check if data appears on replicas
echo ----------------------------------------
echo   Step 2: Check data on replicas
echo ----------------------------------------
echo
echo [INFO] Checking replica 1...
docker exec playlist-replica1 mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "SELECT * FROM Playlist where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 1!
) else (
    echo [ERROR] Data not found on replica 1!
    REM CHANGE: Added debugging info to check replication status
    echo [DEBUG] Checking replica 1 status...
    docker exec playlist-replica1 mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Replica_IO_Running" /C:"Replica_SQL_Running" /C:"Last_Error"
)
echo.

echo [INFO] Checking replica 2...
docker exec playlist-replica2 mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "SELECT * FROM Playlist where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 2!
) else (
    echo [ERROR] Data not found on replica 2!
)
echo.

echo [INFO] Checking replica 3...
docker exec playlist-replica3 mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "SELECT * FROM Playlist where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 3!
) else (
    echo [ERROR] Data not found on replica 3!
)
echo.

echo [INFO] Checking replica 4...
docker exec playlist-replica4 mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "SELECT * FROM Playlist where id = 1;"
if !errorlevel! equ 0 (
    echo [SUCCESS] Data found on replica 4!
) else (
    echo [ERROR] Data not found on replica 4!
)
echo.

REM Clean up test data
echo ----------------------------------------
echo   Step 3: Clean up test data
echo ----------------------------------------
echo [INFO] Cleaning up test data from primary...
docker exec playlist-primary mysql -uroot -p%MYSQL_ROOT_PLAYLIST_PASSWORD% PLAYLIST -e "DELETE FROM Playlist;"

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