#!/bin/sh
# EIS page and config file update script.
# $Revision: 2 
#
#
# NetSnake, 2017-02-26

function do_update(){
	DEST_APP=$1/webapps/`ls $1/webapps/ |awk '{if(NR==1)print $1}'`
	echo "Updating in $DEST_APP"
	cd $DEST_APP
	svn up
	if [ -d $DEST_APP/theme ];then
		cd $DEST_APP/theme
		find ./ -maxdepth 1 -type d   ! -path ./.svn -exec svn up {} \;
	fi
	if [ -d $DEST_APP/WEB-INF/classes ];then
		cd $DEST_APP/WEB-INF/classes
		find ./ -maxdepth 1 -type d   ! -path ./.svn -exec svn up {} \;
	fi
	
	if [ -d $DEST_APP/WEB-INF/jsp ];then
		cd $DEST_APP/WEB-INF/jsp
		find ./ -maxdepth 1 -type d   ! -path ./.svn -exec svn up {} \;
	fi

}

export LC_ALL=en_US.UTF-8
if [ ! $1  ];then
	#Update all tomcat in basic dir.
	
	APP_BASE=$(cd `dirname $0`;pwd)
	echo "Updating All application based at: $APP_BASE"

	cd $APP_BASE;
	for d in `ls | grep tomcat`
	do
		do_update $APP_BASE/$d;
	
	done
else 
	#Update this tomcat.
	DEST=$1;
	if [ ! -d $DEST ];then
		echo "Destination $DEST not exist.";
		exit 2;
	fi
		
	echo "Updating application at: $DEST"
	do_update $DEST;
	
fi

