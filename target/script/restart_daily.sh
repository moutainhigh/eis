#!/bin/sh
if [ ! $1  ];then
	echo "Please special a Config file.";
	exit 1;
fi
CONFIG=$1;
if [ ! -e $DEST ];then
echo "Config file:$DEST not exist.";
exit 2;
fi

for tomcat in `cat $CONFIG|egrep -v "^#"`;do
	CMD="$tomcat/restart.sh"
	echo $CMD
	$CMD
	sleep 5m;
done
