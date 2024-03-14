@Echo off

setlocal

@REM Set the current directory to the installation directory
set INSTALLDIR=%CD%

if exist "%INSTALLDIR%\jdk\bin\java.exe" (
 set JAVA_CMD="%INSTALLDIR%\jdk\bin\java.exe"
) else (
 ECHO Run this program from the installation directory.
 EXIT /B 1
)

%JAVA_CMD% -jar java-bean-journey-1.0.jar %*

@REM Exit using the same code returned from Java
EXIT /B %ERRORLEVEL%