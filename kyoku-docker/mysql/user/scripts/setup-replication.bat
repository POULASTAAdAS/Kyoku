@echo off
setlocal enabledelayedexpansion

echo ================================================
echo           MySQL Replication Setup
echo ================================================

REM Load environment variables from .env file
pushd "%~dp0..\..\.."

if not exist ".env" (
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

echo [INFO] Root password: %MYSQL_ROOT_USER_PASSWORD%
echo [INFO] Replication user: %MYSQL_USER_REPLICATION_USER%

REM Wait for services to be ready
echo [INFO] Waiting 5 seconds for services to be ready...
timeout /t 5 /nobreak >nul

REM Test primary connection
echo [INFO] Testing primary connection...
set "attempts=0"
:test_primary
set /a attempts+=1
docker exec user-primary mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SELECT 1;" >nul 2>&1
if !errorlevel! equ 0 (
    echo [SUCCESS] Primary database is ready!
    goto :setup_replicas
)
if !attempts! geq 10 (
    echo [ERROR] Could not connect to primary database after 10 attempts
    pause
    exit /b 1
)
echo [INFO] Waiting for primary... attempt !attempts!/10
timeout /t 5 /nobreak >nul
goto :test_primary

:setup_replicas
REM Verify replication user exists on primary
echo [INFO] Verifying replication user on primary...
docker exec user-primary mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SELECT User, Host FROM mysql.user WHERE User = '%MYSQL_USER_REPLICATION_USER%';"

REM CHANGE: Fixed MySQL 8.x syntax - use "SHOW BINARY LOG STATUS" instead of "SHOW MASTER STATUS"
echo [INFO] Getting binary log status...
docker exec user-primary mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW BINARY LOG STATUS;"

REM CHANGE: Also show GTID status to verify GTID mode is working
echo [INFO] Checking GTID mode on primary...
docker exec user-primary mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW VARIABLES LIKE 'gtid_mode';"

REM Setup each replica
call :setup_replica user-replica1
call :setup_replica user-replica2  
call :setup_replica user-replica3

echo [SUCCESS] Replication setup complete!
echo [INFO] Checking status in 5 seconds...
timeout /t 5 /nobreak >nul

REM Check final status
echo ================================================
echo            Replication Status Check
echo ================================================
call :check_replica_status user-replica1
call :check_replica_status user-replica2
call :check_replica_status user-replica3

echo [INFO] Setup completed. Press any key to exit.
pause
exit /b 0

REM Function to setup individual replica
:setup_replica
set "replica_name=%1"
echo [INFO] Configuring %replica_name%...

REM Wait for replica to be ready
set "replica_attempts=0"
:test_replica
set /a replica_attempts+=1
docker exec %replica_name% mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SELECT 1;" >nul 2>&1
if !errorlevel! equ 0 (
    echo [SUCCESS] %replica_name% is ready!
    goto :configure_replication
)
if !replica_attempts! geq 10 (
    echo [ERROR] Could not connect to %replica_name%
    goto :eof
)
echo [INFO] Waiting for %replica_name%... attempt !replica_attempts!/10
timeout /t 3 /nobreak >nul
goto :test_replica

:configure_replication
REM CHANGE: First verify GTID mode is ON for the replica
echo [INFO] Checking GTID mode on %replica_name%...
docker exec %replica_name% mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW VARIABLES LIKE 'gtid_mode';"

REM CHANGE: Using MySQL 8.x syntax - CHANGE REPLICATION SOURCE instead of CHANGE MASTER
echo [INFO] Setting up replication for %replica_name%...
docker exec %replica_name% mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "STOP REPLICA; RESET REPLICA ALL; CHANGE REPLICATION SOURCE TO SOURCE_HOST='user-primary', SOURCE_USER='%MYSQL_USER_REPLICATION_USER%', SOURCE_PASSWORD='%MYSQL_USER_REPLICATION_PASSWORD%', SOURCE_AUTO_POSITION=1, GET_SOURCE_PUBLIC_KEY=1; START REPLICA;"

if !errorlevel! equ 0 (
    echo [SUCCESS] %replica_name% configured successfully!
) else (
    echo [ERROR] Failed to configure %replica_name%
    REM CHANGE: Show specific error details for troubleshooting
    echo [DEBUG] Checking for specific errors...
    docker exec %replica_name% mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Last_Error"
)
goto :eof

REM Function to check replica status
:check_replica_status
set "replica_name=%1"
echo ----------------------------------------
echo %replica_name% Status:
echo ----------------------------------------
REM CHANGE: Using MySQL 8.x syntax - SHOW REPLICA STATUS instead of SHOW SLAVE STATUS
docker exec %replica_name% mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Replica_IO_Running" /C:"Replica_SQL_Running" /C:"Seconds_Behind_Source" /C:"Last_Error"
echo.
goto :eof