@echo off
setlocal enabledelayedexpansion

echo ================================================
echo       MySQL PLAYLIST Database Setup
echo ================================================
REM Generate configuration files from templates
echo  ----------------------------------------
echo    Step 1: Generate configuration files
echo  ----------------------------------------
call mysql\playlist\scripts\generate-configs.bat

if !errorlevel! neq 0 (
    echo [ERROR] Failed to generate configuration files!
    pause
    exit /b 1
)

echo.

REM Run replication setup
echo ----------------------------------------
echo   Step 2: Setting up replication
echo ----------------------------------------
call mysql\playlist\scripts\setup-replication.bat

echo.
echo ----------------------------------------
echo   Step 3: Testing replication
echo ----------------------------------------
echo [INFO] Running replication test...
call mysql\playlist\scripts\test-replication.bat

echo.
echo ================================================
echo              Setup Complete!
echo ================================================
echo [INFO] Your MySQL Master-Slave replication is now configured.
echo [INFO] You can use the following scripts for monitoring:
echo.
echo   mysql\playlist\scripts\check-replication.bat  - Check replication status
echo   mysql\playlist\scripts\test-replication.bat   - Test replication functionality
echo   generate-configs.bat                           - Regenerate config files
echo.
echo [INFO] Connection details:
echo   Primary (Master): localhost:1010
echo   Replica 1:        localhost:1011  
echo   Replica 2:        localhost:1012
echo   Replica 3:        localhost:1013
echo   Replica 4:        localhost:1014
echo   ProxySQL:         localhost:1015 (MySQL) / localhost:1016 (Admin)
echo.
echo [INFO] Press any key to exit.
pause