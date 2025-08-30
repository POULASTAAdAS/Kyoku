@echo off
setlocal enabledelayedexpansion

echo ================================================
echo       MySQL PLAYLIST Database Setup
echo ================================================
REM Run replication setup
echo ----------------------------------------
echo   Step 2: Setting up replication
echo ----------------------------------------
call mysql\content\scripts\setup-replication.bat

echo.
echo ----------------------------------------
echo   Step 3: Testing replication
echo ----------------------------------------
echo [INFO] Running replication test...
call mysql\content\scripts\test-replication.bat

echo.
echo ================================================
echo              Setup Complete!
echo ================================================
echo [INFO] Your MySQL Master-Slave replication is now configured.
echo [INFO] You can use the following scripts for monitoring:
echo.
echo   mysql\content\scripts\check-replication.bat  - Check replication status
echo   mysql\content\scripts\test-replication.bat   - Test replication functionality
echo   mysql\content\scripts\generate-configs.bat   - Regenerate config files
echo.
echo [INFO] Connection details:
echo   Primary (Master): localhost:1030
echo   Replica 1:        localhost:1031  
echo   Replica 2:        localhost:1032
echo   Replica 3:        localhost:1033
echo   Replica 4:        localhost:1034
echo   Replica 5:        localhost:1035
echo   ProxySQL:         localhost:1036 (MySQL) / localhost:1037 (Admin)
echo.
echo [INFO] Press any key to exit.
pause