#!/bin/sh
LIB_PATH=/var/chaoka.cn/tomcat-front-7.0.42-1/webapps/v4-front/WEB-INF/lib
cd LIB_PATH
java -cp commons-logging-1.1.jar:eis-v4-current-front.jar:commons-httpclient-3.1.jar:commons-codec-1.9.jar  com.maicard.front.socket.SocketDaemon