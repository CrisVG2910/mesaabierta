@ECHO OFF
SETLOCAL EnableExtensions
SET "PROJECT_ROOT=%~dp0"
SET "MAVEN_BIN=%PROJECT_ROOT%.mvn\apache-maven-3.9.11\bin\mvn.cmd"
IF NOT EXIST "%MAVEN_BIN%" (
  powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%PROJECT_ROOT%.mvn\bootstrap-maven.ps1"
  IF ERRORLEVEL 1 EXIT /B 1
)
CALL "%MAVEN_BIN%" %*
EXIT /B %ERRORLEVEL%
