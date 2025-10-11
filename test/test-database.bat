@echo off
echo ================================================
echo FruitStore Database Connection Test
echo ================================================

echo.
echo Compiling Java files...

:: Compile all Java files (from test/ directory, need to go up one level)
javac -cp "..\ref_lib\*" -d ..\build\classes ..\src\java\config\*.java ..\src\java\utils\*.java ..\src\java\daos\*.java ..\src\java\models\*.java DatabaseTest.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Make sure you have:
    echo 1. Java JDK installed
    echo 2. MySQL Connector JAR in ..\ref_lib folder
    echo 3. All source files exist in ..\src\java
    pause
    exit /b 1
)

echo Compilation successful!
echo.

echo Copying resource files...
if not exist "..\build\classes" mkdir "..\build\classes"
copy "..\src\java\resources\database.properties" "..\build\classes\" >nul 2>&1
copy "..\src\java\resources\Database.sql" "..\build\classes\" >nul 2>&1

echo Running database test...
echo.

:: Run the test
java -cp "..\ref_lib\*;..\build\classes" DatabaseTest

echo.
echo Test completed. Press any key to exit...
pause >nul
