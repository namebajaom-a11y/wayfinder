@echo off
echo Running Wayfinder Auth Service...
echo.
cd /d "%~dp0src\main\java"
set MONGODB_URI=mongodb://localhost:27017
set MONGODB_DB=wayfinderDB
call mvn compile -q
java -cp target/classes wayfinder.AuthService
pause