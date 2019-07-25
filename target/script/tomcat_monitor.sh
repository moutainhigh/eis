#!/bin/sh
. /etc/profile

PORT=0;
STATUS=-1;
USAGE_DESC="Usage $0 tomcat dir.\n"
LOG_FILE="../tomcat_monitor.log"

function get_port(){
	if [ ! -e $DEST/conf/server.xml ]; then
		echo "$DEST/conf/server.xml file not found!";
		exit 1;
	fi
	PORT=`grep -E  "<Server port=\"([0-9]+)\" shutdown=\"SHUTDOWN\">" $DEST/conf/server.xml |awk -F '"'  '{print $2}'`
	if [[ $PORT -eq 0 ]];then
		echo "Tomcat port not found, exit."
		exit 2;
	fi
	PORT=`expr $PORT + 1000`
}

function check_connect(){
	if [[ $PORT -eq 443 ]]; then
		PROTOCOL="https"
	else 
		PROTOCOL="http"
	fi
	HOST=$PROTOCOL://localhost:$PORT;
	echo "Try connect to $HOST";
	curl -4 --fail /dev/null  $HOST
	STATUS=$?;
#	if [[ $PROTOCAL="https" ]]; then
#		if [[ $STATUS -eq 60 || $STATUS -eq 0 ]]; then
#			STATUS=0;
#		fi
#	fi
}


# Check parameter
if [ ! $1  ] 
then
	echo -e $USAGE_DESC;
	exit 1
fi
DEST=$1
if [ ! -d $DEST ]
then
	echo "Destination $DEST not exist.";
	exit 2
fi


if [ ! $2 ]; then
	get_port;
else 
	PORT=$2
fi

check_connect;
echo "Check status:$STATUS";
if [[ $STATUS -ne 7 ]]; then
	echo "`date`: $DEST is OK"
	echo "`date` $DEST is OK" >> $DEST/$LOG_FILE;

else
	ERROR_MSG="$DEST is BAD, try restart at `date`"
	echo $ERROR_MSG
	echo $ERROR_MSG >> $DEST/$LOG_FILE;
	$DEST/restart.sh
fi
