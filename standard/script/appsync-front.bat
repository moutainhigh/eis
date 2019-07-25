@echo off
set WORK=D:\work\tomcats\yeele\tomcat-yeele-front-7.0.26
echo "Install chaoka current..."
set SRC=D:\NetSnake\Source\changxiu-eis\v4\CURRENT
cd %SRC%\standard
call ant jar

cd %SRC%\transaction-system
call ant jar
cd %SRC%\front
call ant jar

set DEST=%WORK%\webapps\chaoka-current
xcopy  /y /s /q /D %SRC%\front\WebContent\front\chaoka\* %DEST%\
xcopy  /y /q /D %SRC%\front\eis-v4-current-front.jar %DEST%\WEB-INF\lib\

xcopy  /y /q /D %SRC%\standard\eis-v4-current-standard.jar %DEST%\WEB-INF\lib\
xcopy  /y /q /D %SRC%\transaction-system\eis-v4-current-ts.jar %DEST%\WEB-INF\lib\

xcopy  /y /q /D %SRC%\standard\lib\*.jar %DEST%\WEB-INF\lib\
xcopy  /y /q /D %SRC%\standard\lib\mysql-connect*.jar %WORK%\lib\
xcopy  /y /D %SRC%\standard\src\*.xml %DEST%\WEB-INF\classes\
xcopy  /y /q /D %SRC%\standard\src\ehcache.* %DEST%\WEB-INF\classes\
del /q %DEST%\WEB-INF\lib\servlet-api.jar
del /q %DEST%\WEB-INF\lib\mysql-connect*.jar

echo "Install yeele current..."
set DEST=%WORK%\webapps\yeele-current
xcopy  /y /s /q /D %SRC%\front\WebContent\front\youquyou\* %DEST%\
xcopy  /y /q /D %SRC%\front\eis-v4-current-front.jar %DEST%\WEB-INF\lib\

xcopy  /y /q /D %SRC%\standard\eis-v4-current-standard.jar %DEST%\WEB-INF\lib\
xcopy  /y /q /D %SRC%\transaction-system\eis-v4-current-ts.jar %DEST%\WEB-INF\lib\

xcopy  /y /q /D %SRC%\standard\lib\*.jar %DEST%\WEB-INF\lib\
xcopy  /y /q /D %SRC%\standard\lib\mysql-connect*.jar %WORK%\lib\
xcopy  /y /D %SRC%\standard\src\*.xml %DEST%\WEB-INF\classes\
xcopy  /y /q /D %SRC%\standard\src\ehcache.* %DEST%\WEB-INF\classes\
del /q %DEST%\WEB-INF\lib\servlet-api.jar
del /q %DEST%\WEB-INF\lib\mysql-connect*.jar


echo "Install yeele stable..."
set SRC=D:\NetSnake\Source\changxiu-eis\v4\STABLE
cd %SRC%
call ant jar

set DEST=%WORK%\webapps\yeele-stable
xcopy  /y /s /q /D %SRC%\WebContent\front\yeele\* %DEST%\
xcopy  /y /q /D %SRC%\changxiu-*.jar %DEST%\WEB-INF\lib\
del /q %DEST%\WEB-INF\lib\servlet-api.jar
del /q %DEST%\WEB-INF\lib\mysql-connect*.jar

cd %WORK%
del /q %WORK%\logs\*
set CATALINA_HOME=.
