@echo off
setlocal enabledelayedexpansion

set JAVA_EXEC=java
if not defined JAVA_HOME (
  set JAVA_EXEC=java
  echo No Java Home was found. Using current path. If execution fails please install Java and make sure it is in the search path or exposed via the JAVA_HOME environment variable.
) else (
  echo JAVA_HOME found, using !JAVA_HOME!
  set JAVA_EXEC=!JAVA_HOME!\bin\java
)

if not defined DIAG_JAVA_OPTIONS (
  set DIAG_JAVA_OPTIONS=-Xmx2000m
)

"%JAVA_EXEC%" %DIAG_JAVA_OPTIONS% -cp .\;.\lib\* com.elastic.support.diagnostics.ScrubApp %*

endlocal
