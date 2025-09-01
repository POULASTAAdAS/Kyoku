@echo off
setlocal enabledelayedexpansion

echo ================================================
echo       MySQL USER Database Setup
echo ================================================
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
echo   mysql\user\scripts\generate-configs.bat   - Regenerate config files
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