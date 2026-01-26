@ECHO OFF
SETLOCAL

SET MAVEN_PROJECTBASEDIR=%~dp0
SET WRAPPER_DIR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper
SET PROPS_FILE=%WRAPPER_DIR%\maven-wrapper.properties
SET JAR_FILE=%WRAPPER_DIR%\maven-wrapper.jar

IF NOT EXIST "%PROPS_FILE%" (
  ECHO Missing %PROPS_FILE%
  EXIT /B 1
)

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%PROPS_FILE%") DO (
  IF "%%A"=="distributionUrl" SET distributionUrl=%%B
  IF "%%A"=="wrapperUrl" SET wrapperUrl=%%B
)

IF NOT EXIST "%JAR_FILE%" (
  POWERSHELL -NoProfile -Command "(New-Object Net.WebClient).DownloadFile('%wrapperUrl%','%JAR_FILE%')" || EXIT /B 1
)

IF "%JAVA_HOME%"=="" (
  SET JAVA_EXE=java
) ELSE (
  SET JAVA_EXE=%JAVA_HOME%\bin\java
)

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -classpath "%JAR_FILE%" org.apache.maven.wrapper.MavenWrapperMain -Dmaven.home="%MAVEN_PROJECTBASEDIR%" "-Dmaven.wrapper.distributionUrl=%distributionUrl%" %*

ENDLOCAL
