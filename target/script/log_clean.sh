#!/bin/sh
if [ ! $1  ];then
	APP_BASE=$(cd `dirname $0`;pwd)
else 
	APP_BASE=$1
fi
echo $APP_BASE
find $APP_BASE  -name \*.out  -exec cp /dev/null {} \;
find $APP_BASE -name \*.log  -exec cp /dev/null {} \;
