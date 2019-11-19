@echo off & setlocal enabledelayedexpansion

set SERVER_NAME=dynamicreport.feature.provider

if "%1" == "" (
	SET APP_ENV=product
) else (
	SET APP_ENV=%1
)

cd ..
set DEPLOY_PARENT_DIR=%cd%
set LOG_DIR=%DEPLOY_PARENT_DIR%\applog\%SERVER_NAME%\logs
set LOG_CONFIG=%DEPLOY_PARENT_DIR%\config\%APP_ENV%\logback.xml
set CONF_DIR=%DEPLOY_PARENT_DIR%\config\%APP_ENV%\application.properties

cd bin
java -Dlogging.path=%LOG_DIR% -Dlogging.config=%LOG_CONFIG% -Dspring.config.location=%CONF_DIR% -cp ..\lib\* com.lakala.dynamicreport.Application
pause