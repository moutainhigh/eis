@echo off
set WORK=D:\work\tomcats\yeele\tomcat-yeele-boss-7.0.26
set SRC=D:\NetSnake\Source\eis\CURRENT
cd %SRC%\standard
call ant jar

cd %SRC%\transaction-system
call ant jar
cd %SRC%\boss
call ant jar

echo "Install chaoka boss current..."
set DEST=%WORK%\webapps\chaoka-current

xcopy  /y /s /D %SRC%\boss\WebContent\boss\chaoka\* %DEST%\
xcopy  /y /D %SRC%\boss\eis-v4-current-boss.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\standard\eis-v4-current-standard.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\transaction-system\eis-v4-current-ts.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\standard\lib\*.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\standard\lib\mysql-connect*.jar %WORK%\lib\
xcopy  /y /D %SRC%\standard\src\*.xml %DEST%\WEB-INF\classes\
xcopy  /y /D %SRC%\standard\src\ehcache.* %DEST%\WEB-INF\classes\

del /q %DEST%\WEB-INF\lib\servlet-api.jar
del /q %DEST%\WEB-INF\lib\mysql-connect*.jar


echo "Install yeele boss current..."
set DEST=%WORK%\webapps\yeele-current

xcopy  /y /s /D %SRC%\boss\WebContent\boss\yeele\* %DEST%\
xcopy  /y /D %SRC%\boss\eis-v4-current-boss.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\standard\eis-v4-current-standard.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\transaction-system\eis-v4-current-ts.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\standard\lib\*.jar %DEST%\WEB-INF\lib\
xcopy  /y /D %SRC%\standard\lib\mysql-connect*.jar %WORK%\lib\
xcopy  /y /D %SRC%\standard\src\*.xml %DEST%\WEB-INF\classes\
xcopy  /y /D %SRC%\standard\src\ehcache.* %DEST%\WEB-INF\classes\

del /q %DEST%\WEB-INF\lib\servlet-api.jar
del /q %DEST%\WEB-INF\lib\mysql-connect*.jar

cd %WORK%
del /q %WORK%\logs\*
set CATALINA_HOME=.
