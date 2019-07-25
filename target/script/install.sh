#!/bin/sh
# EIS install script.
# $Revision: 5
#
#
# NetSnake, 2018-02-5
#
# EIS安装脚本,NetSnake,2015-11-01

INSTALL_TYPE=0;
RELEASE_TYPE="current";
TARGET_TYPE="";
MARJOR=5;
REV=$MARJOR.0.0;

CONFLICT_ACCEPT="theirs-full";

IGNORE_MODULE="gc";

function _clean(){
	rm -rf $DEST_APP/WEB-INF/classes/config;
	rm -f $DEST_APP/WEB-INF/lib/servlet-api.jar;
	rm -f $DEST_APP/WEB-INF/lib/mysql-connector-*.jar;
	rm -f $DEST_APP/WEB-INF/lib/tomcat-jdbc.jar;

}

function get_version(){
	VERSION_CHECK_DIR="";
	if [ -d target ];then
		VERSION_CHECK_DIR="target";
	else 
		VERSION_CHECK_DIR="lib";
	fi
		cd $VERSION_CHECK_DIR;
	REV=`svn info|grep "^Revision"|sed s/Revision:\ //|awk '{printf("%05s\n",$0)}'`;
	MINOR=`echo $REV|awk '{print substr($0,1,2)}'`;
	BUILD=`echo $REV|awk '{print substr($0,3)}'`;
	REV=$MARJOR.$MINOR.$BUILD;
	unset MINOR;
	unset BUILD;
	cd ..;
}

function write_version(){
	SYSTEM_PROPERTIES_FILE=$DEST_APP/WEB-INF/classes/server-special.properties;
	if [ ! -e $SYSTEM_PROPERTIES_FILE ]; then
		SYSTEM_PROPERTIES_FILE=$DEST_APP/WEB-INF/classes/server-special-v4.properties;
	fi
		if [ ! -e $SYSTEM_PROPERTIES_FILE ]; then
			echo "System properties file NOT found! $DEST_FILE";
	exit 5;
	fi

		sed /systemVersion/d $SYSTEM_PROPERTIES_FILE > ~/rev;
	echo  "systemVersion=$REV" >> ~/rev;
	cp ~/rev $SYSTEM_PROPERTIES_FILE;
	rm -f ~/rev;

}


function modify_catalina(){
	CATALINA_SH=$DEST/bin/catalina.sh;
	
	if [ ! -e $CATALINA_SH ]; then
		echo "$CATALINA_SH file not found!";
		exit 5;
	fi

	sed -i 's/UMASK="0027"/UMASK="0002"/g' $CATALINA_SH

}




function do_partner(){
	if [ -d $DEST_APP/WEB-INF/jsp/common ];then
#echo "svn update $DEST_APP/WEB-INF/jsp/common/"
		svn up $DEST_APP/WEB-INF/jsp/common/;
	else 
		mkdir -p $DEST_APP/WEB-INF/jsp/;
		echo "Please checkout module from svn path eis/DEVELOP/wpto/WebContent/jsp/partner/common to $DEST_APP/WEB-INF/jsp/common/";
	fi
	
	cd $DEST_APP/WEB-INF/jsp/;
	find ./ -maxdepth 1  -type d |grep -v .svn |xargs svn up;

	if [ -d $DEST_APP/ueditor ];then
		svn up $DEST_APP/ueditor;
	else 
		echo "Please checkout module from svn path /eis/DEVELOP/wpto/WebContent/ueditor to $DEST_APP/ueditor/";

	fi

	if [ -d $DEST_APP/theme/ ];then
		echo "svn update $DEST_APP/theme/";
		svn up  $DEST_APP/theme/;
	else 
		echo "Please checkout module from svn path /eis/DEVELOP/wpto/WebContent/theme/partner to $DEST_APP/theme/";
	fi



}


#Build module function
function build_module(){
	if [ -d $1 ];then
		echo "Build module $1."
		cd $1;

		rm -f *.jar;
		rm -f $JAR_FILE;
		if [ $INSTALL_TYPE == 1 ];then
			ant clean;
		fi
		svn --accept $CONFLICT_ACCEPT up;
		if [ -f pom.xml ];then
			mvn install;
		else 
			ant jar;
		fi
		if [ -d lib ];then
			cp lib/*.* $DEST_APP/WEB-INF/lib/;
		fi
		cd ..;
	else 
		echo -n ".";
	fi

	
}

function make_dir(){
	if [ ! -d $DEST_APP/WEB-INF/jsp ];then
	mkdir -p $DEST_APP/WEB-INF/jsp;
	fi
}

COPY_STANDARD_CONFIG=true;

echo "WARNING: This script must running in a correct source directory!";

USAGE_DESC="Usage: script INSTALL_DESTINATION [INSTALL_TYPE] \nINSTALL_TYPE: 1 to clean all project\n2 to not build common and standard module\n";

# Check parameter
if [ ! $1  ];then
echo -e $USAGE_DESC;
exit 1;
fi
DEST=$1;
if [ ! -d $DEST ];then
echo "Destination $DEST not exist.";
exit 2;
fi

# Init
export LC_ALL=en_US.UTF-8;
svn  --accept $CONFLICT_ACCEPT up  lib;
svn  --accept $CONFLICT_ACCEPT up target;
svn  --accept $CONFLICT_ACCEPT up api;

if [ -d internal ];then
svn  --accept $CONFLICT_ACCEPT up internal;
fi

chmod +x target/script/install.sh;
chmod -f +x common/crypt.sh;
chmod -f +x standard/crypt.sh;
cp -f target/eis-bootstrap.jar $DEST/lib/;
cp -f target/script/restart.sh $DEST;
chmod +x $DEST/restart.sh;

START_TIME=`date +%s`;

DEST_APP=$DEST/webapps/`ls $DEST/webapps/ |awk '{if(NR==1)print $1}'`
echo "APPLICATION LOCATION:$DEST_APP";


SRCDIR=`pwd`;
if [[ ! -d $SRCDIR/target && ! -d $SRCDIR/lib ]];then
echo "Source directory $SRCDIR is not a valid source dir!";
exit 7
			fi


#Clean all jar and classes 
svn up $DEST_APP/WEB-INF/lib/
if [ -z $2 ];then
	CLEAR_TYPE=$INSTALL_TYPE;
else
	INSTALL_TYPE=$2;
fi

if [ $INSTALL_TYPE -eq 1 ];then
	rm -f $DEST_APP/WEB-INF/lib/eis-*.jar;
	echo "Clear old class file and jar files...";
fi

# Commit local conflict to server, default is accept both conflict from server
if [ -z $3 ];then
	CONFLICT_ACCEPT="theirs-full";
elif [ $3 -eq 1 ];then
	CONFLICT_ACCEPT="mine-full";
	echo "WARNING: conflict mode changed to LOCAL first! local changes will be submitted to svn server!";
fi

get_version;
make_dir;
modify_catalina;

if [ -e bootstrap ];then
	cd bootstrap;svn up; ant jar;cp ../target/eis-bootstrap.jar $DEST/lib;cd ..;
fi


################ Find all modules in dir, make sure common and standard module building first. ####################
MODULES=`find ./ -maxdepth 2 -name build.xml|egrep -v 'common|standard'|awk -F '/' '{printf("%s ",$2)}'`;

if [ -d standard ];then
	MODULES="standard  $MODULES"
fi
if [ -d common ];then
	MODULES="common $MODULES"
fi

echo "Build module: $MODULES";
for MODULE_NAME in $MODULES
do
	if [ $INSTALL_TYPE -eq 2 ];then
		if [ $MODULE_NAME == "common" ];then
			echo "Ignore build module $MODULE_NAME";
			continue;
		fi
	fi
	build_module $MODULE_NAME;
done
			
if [ -e target ];then
	cp target/eis-*.jar $DEST_APP/WEB-INF/lib/;
fi
if [ -e internal ];then
	cp internal/eis-*.jar $DEST_APP/WEB-INF/lib/;
fi



if [[ $DEST_APP =~ "partner" ]];then
	do_partner;
fi

if [ -e DEST/eis.product ]; then
	#Production env
	cp target/example/logback-product.xml $DEST_APP/WEB-INF/classes;
fi

cd $DEST_APP
svn up;

svn up $DEST_APP/WEB-INF/classes;
svn up $DEST_APP/WEB-INF/classes/sconfig;
svn up $DEST_APP/WEB-INF/classes/pconfig;

_clean;

cd $SRCDIR;
export LC_ALL=zh_CN.GBK;

write_version;
CLOSE_TIME=`date +%s`;
USE_TIME=`expr $CLOSE_TIME - $START_TIME`;
if [ -e submit.sh ];then
	./submit.sh
fi
echo ">>>>>>>>>>>>>>> Build success for version: $REV, using time $USE_TIME secs. <<<<<<<<<<<<<<<<<<<<";

