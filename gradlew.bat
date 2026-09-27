@echo off
setlocal
set APP_HOME=%~dp0
if not exist "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" (
  echo Missing gradle\wrapper\gradle-wrapper.jar. Run .\bootstrap-wrapper.ps1 first.
  exit /b 1
)
java %JAVA_OPTS% %GRADLE_OPTS% -jar "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" %*
endlocal
