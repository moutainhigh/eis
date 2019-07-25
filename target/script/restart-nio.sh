#!/bin/sh
# EIS tomcat restart script.
# $Revision: 3
#
#
# NetSnake, 2017-02-26

. /etc/profile
PROG_NAME=`basename $0`

export LC_ALL=zh_CN
WORK=`echo $0|sed s/$PROG_NAME//g`;
if [ ! -e $WORK/classes/server-special.properties ];then
        WORK="`pwd`/"
fi

cd $WORK
WORK=`pwd`;

if [ ! -e classes/server-special.properties ];then
        echo "Invalid server dir: $WORK"
        exit
fi
echo "Restart EIS NIO server:" + $WORK

#Waiting a few seconds to shutdown.
for (( i=1; i<3; i++ ));
do
        process_cnt=`ps -ef|grep java|grep -v grep|grep $WORK|wc -l`;
        if [ $process_cnt -gt 0 ];then
		echo "Waiting server shutdown working complete...$i"
        else 
               break
        fi
        sleep 1;
done

#Kill process when timeout
process_cnt=`ps -ef|grep java|grep -v grep|grep $WORK|wc -l`;
if [ $process_cnt -gt 0 ];then
	echo "Try force kill process..."
	ps -ef|grep java|grep -v grep|grep $WORK|awk '{print $2}'|xargs sudo kill -9 >/dev/null 2>&1
	sleep 1;
	ps -ef|grep java|grep -v grep|grep $WORK|awk '{print $2}'|xargs sudo kill -9 >/dev/null 2>&1
fi
if [ -z $1 ];then
        echo ""
elif [  $1 -eq 2 ];then
	echo "Deleting log file"
	rm -f logs/*
	rm -f nohup.out
fi

#Remove unneed jars
if [ -e remove_jars.txt ];then
	OLD_PATH=`pwd`;
	REMOVE_JARS=`cat remove_jars.txt`;
	DIR=lib;
	if [ -d $DIR ];then
			cd  $DIR;
			echo "Rmove jar file: $REMOVE_JARS in path: $DIR";
			rm -f $REMOVE_JARS;
			cd $OLD_PATH;
	fi
fi

nohup java -cp $WORK/classes/:$WORK/lib/*  com.maicard.nio.netty.Bootstrap context-nio-server.xml  &
echo "Done"
