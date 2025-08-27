@echo off
setlocal enabledelayedexpansion

echo ================================================
echo        MySQL Replication Status Check
echo ================================================

REM Load environment variables from .env file
pushd "%~dp0..\..\.."

if not exist ".env" (
    echo [ERROR] .env file not found in root directory.
    pause
    exit /b 1
)

echo [INFO] Loading environment variables...
for /f "usebackq tokens=1,2 delims==" %%i in (".env") do (
    if not "%%i"=="" if not "%%i:~0,1%"=="#" (
        set "%%i=%%j"
    )
)

echo ----------------------------------------
echo           Master Status
echo ----------------------------------------
docker exec db-primary mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW MASTER STATUS;"

echo.
echo ----------------------------------------
echo         Replica 1 Status
echo ----------------------------------------
docker exec db-replica1 mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Replica_IO_Running" /C:"Replica_SQL_Running" /C:"Seconds_Behind_Source" /C:"Last_Error"

echo.
echo ----------------------------------------
echo         Replica 2 Status  
echo ----------------------------------------
docker exec db-replica2 mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Replica_IO_Running" /C:"Replica_SQL_Running" /C:"Seconds_Behind_Source" /C:"Last_Error"

echo.
echo ----------------------------------------
echo         Replica 3 Status
echo ----------------------------------------
docker exec db-replica3 mysql -uroot -p%MYSQL_ROOT_USER_PASSWORD% -e "SHOW REPLICA STATUS\G" | findstr /C:"Replica_IO_Running" /C:"Replica_SQL_Running" /C:"Seconds_Behind_Source" /C:"Last_Error"

echo.
echo ----------------------------------------
echo         ProxySQL Stats
echo ----------------------------------------
docker exec user-proxysql mysql -h127.0.0.1 -P6032 -uadmin -padmin -e "SELECT hostgroup_id, hostname, port, status, weight FROM mysql_servers;" 2>nul

if !errorlevel! neq 0 (
    echo [WARNING] ProxySQL may not be ready yet or configuration issue detected.
)

echo.
echo [INFO] Status check completed. Press any key to exit.
pause