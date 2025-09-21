@echo off
REM ==========================
REM Configuration
REM ==========================

REM Name of your application
set APP_NAME=GreatShooter

REM Main class of your Java app
set MAIN_CLASS=org.example.Main

REM Jar file to package
set JAR_FILE=great-shooter-1.0-SNAPSHOT.jar

REM Output folder
set OUTPUT_DIR=dist

REM Minimum Java version required
set JAVA_VERSION=17

REM Optional JVM options
set JAVA_OPTIONS=-Xmx512m

REM ==========================
REM Check JAVA_HOME
REM ==========================
if "%JAVA_HOME%"=="" (
    echo ERROR: JAVA_HOME is not set.
    exit /b 1
)

set JPACKAGE=%JAVA_HOME%\bin\jpackage.exe

if not exist "%JPACKAGE%" (
    echo ERROR: jpackage.exe not found in JAVA_HOME. Make sure you have JDK 17+.
    exit /b 1
)

REM ==========================
REM Create output folder
REM ==========================
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

REM ==========================
REM Run jpackage
REM ==========================
"%JPACKAGE%" ^
    --input build\libs ^
    --name "%APP_NAME%" ^
    --main-jar "%JAR_FILE%" ^
    --main-class "%MAIN_CLASS%" ^
    --type app-image ^
    --dest "%OUTPUT_DIR%" ^
    --java-options "%JAVA_OPTIONS%" ^
    --app-version 1.0 ^
    --vendor "Your Name" ^
    --add-modules java.base,java.logging,java.desktop 

if %ERRORLEVEL% neq 0 (
    echo jpackage failed!
    exit /b 1
)

echo ==========================
echo Packaging complete!
echo Find your installer in %OUTPUT_DIR%
echo ==========================
pause
