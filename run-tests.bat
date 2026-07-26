@echo off
cd /d C:\Users\ippach\Desktop\java-project-99
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.9.10-hotspot"
set "PATH=%JAVA_HOME%\bin;C:\Gradle\gradle-9.2.1\bin;%PATH%"
set JAVA_TOOL_OPTIONS=
call C:\Gradle\gradle-9.2.1\bin\gradle.bat --no-daemon test
echo EXITCODE=%ERRORLEVEL%
