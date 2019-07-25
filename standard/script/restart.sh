#!/bin/sh
. /etc/profile
PROG_NAME="restart.sh"

export LC_ALL=zh_CN
if [ -e conf/server.xml ];then
	WORK="`pwd`/"
else
	WORK=`echo $0|sed s/$PROG_NAME//g`;
fi

cd $WORK
if [ ! -e conf/server.xml ];then
	echo "Invalid tomcat dir: $WORK"
	exit
fi

./bin/shutdown.sh
for (( i=1; i<6; i++ ));
do
	process_cnt=`ps -ef|grep java|grep -v grep|grep $WORK|wc -l`;
	if [ $process_cnt -gt 0 ];then
		echo "Try kill process...$i"
		ps -ef|grep java|grep -v grep|grep $WORK|awk '{print $2}'|xargs sudo kill -9
	else 
		break
	fi
	sleep 1;
done

rm -f logs/*
./bin/startup.sh

