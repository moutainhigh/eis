#!/bin/sh
# EIS tomcat restart script.
# $Revision: 3
#
#
# NetSnake, 2017-02-26

. /etc/profile
PROG_NAME="restart.sh"

export LC_ALL=zh_CN
WORK=`echo $0|sed s/$PROG_NAME//g`;
if [ ! -e $WORK/conf/server.xml ];then
        WORK="`pwd`/"
fi

cd $WORK
WORK=`pwd`;

if [ ! -e conf/server.xml ];then
        echo "Invalid tomcat dir: $WORK"
        exit
fi
echo "Restart tomcat:" + $WORK

./bin/shutdown.sh
#Waiting a few seconds to shutdown.
for (( i=1; i<11; i++ ));
do
        process_cnt=`ps -ef|grep java|grep -v grep|grep $WORK|wc -l`;
        if [ $process_cnt -gt 0 ];then
		echo "Waiting tomcat shutdown working complete...$i"
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

rm -f logs/manager*
rm -f logs/localhost*
rm -f logs/host*
rm -f logs/catalina.*.log

if [ -z $1 ];then
        echo ""
		mv logs/catalina.out logs/catalina.`date +%Y%m%d%H%M%S`
elif [  $1 -eq 2 ];then
	if [ ! -e eis.product ];then
		echo "Deleting log file"
		rm -f logs/*
	fi
fi
#Remove unneed jars
if [ -e remove_jars.txt ];then
	OLD_PATH=`pwd`;
	REMOVE_JARS=`cat remove_jars.txt`;
	for webapp in `ls webapps`;do
		DIR=webapps/$webapp/WEB-INF/lib;
		if [ -d $DIR ];then
			cd  $DIR;
			echo "Rmove jar file: $REMOVE_JARS in path: $DIR";
			rm -f $REMOVE_JARS;
			cd $OLD_PATH;
		fi
		done
fi

if [ -e ../remove_jars.txt ];then
	OLD_PATH=`pwd`;
	REMOVE_JARS=`cat ../remove_jars.txt`;
	for webapp in `ls webapps`;do
		DIR=webapps/$webapp/WEB-INF/lib;
		if [ -d $DIR ];then
			cd  $DIR;
			echo "Rmove jar file: $REMOVE_JARS in path: $DIR";
			rm -f $REMOVE_JARS;
			cd $OLD_PATH;
		fi
		done
fi


./bin/startup.sh
