@echo off
set WORK=D:\work\tomcats\yeele\tomcat-yeele-partner-7.0.26
echo "Install partner yeele current..."
set SRC=D:\NetSnake\Source\changxiu-eis\v4\CURRENT
cd %SRC%\standard
call ant jar

cd %SRC%\partner
call ant jar

set DEST=%WORK%\webapps\yeele-current
xcopy  /y /s /q /D %SRC%\partner\WebContent\partner\yeele\* %DEST%\
xcopy  /y /q /D %SRC%\partner\eis-v4-current-partner.jar %DEST%\WEB-INF\lib\

xcopy  /y /q /D %SRC%\standard\eis-v4-current-standard.jar %DEST%\WEB-INF\lib\

xcopy  /y /q /D %SRC%\standard\lib\*.jar %DEST%\WEB-INF\lib\
xcopy  /y /q /D %SRC%\standard\lib\mysql-connect*.jar %WORK%\lib\
xcopy  /y /q /D %SRC%\standard\src\*v4-all.xml %DEST%\WEB-INF\classes\
xcopy  /y /q /D %SRC%\standard\src\ehcache.* %DEST%\WEB-INF\classes\
del /q %DEST%\WEB-INF\lib\servlet-api.jar
del /q %DEST%\WEB-INF\lib\mysql-connect*.jar

cd %WORK%
del /q %WORK%\logs\*
set CATALINA_HOME=.
