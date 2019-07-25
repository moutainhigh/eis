#!/bin/sh
# EIS install script.
# $Revision: 3
#
#
# NetSnake, 2017-02-26
#
# EIS安装脚本,NetSnake,2015-11-01

SVN_PREFIX="svn://svn.maicard.com/eis/DEVELOP";
INSTALL_TYPE=0;
RELEASE_TYPE="current";
TARGET_TYPE="";
MARJOR=5;
REV=$MARJOR.0.0;
MODULES="cloud common common-cloud flow standard share up ec wpt wpto gp nio";

CONFLICT_ACCEPT="theirs-full";



#Build module function
function build_module(){
	if [ -d $1 ];then
	echo "Build module $1."
	cd $1;

	rm -f *.jar;
	rm -f $JAR_FILE;
	if [ $INSTALL_TYPE == 1 ];then
	ant -q clean;
	fi
	svn -q --accept $CONFLICT_ACCEPT up;
	ant jar;

	if [ -d lib ];then
	cp lib/*.* $DEST/lib/;
	fi
	cd ..;
	else
	echo "Module $1 not exist.";
	fi

	JAR_FILE=target/eis-$1.jar;
	if [ -e $JAR_FILE ];then
	cp  $JAR_FILE $DEST/lib/;
	fi

	JAR_FILE=internal/eis-$1.jar;
	if [ -e $JAR_FILE ];then
	cp  $JAR_FILE $DEST/lib/;
	fi
}
function make_dir(){

	if [ ! -d $DEST/classes/sconfig ];then
	svn checkout svn://svn.maicard.com/eis/DEVELOP/target/sconfig $DEST/classes/sconfig;
	fi

	#if [ ! -d $DEST/classes/pconfig ];then
	#	mkdir -p $DEST/classes/pconfig
	#fi

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
svn  --accept $CONFLICT_ACCEPT up -q  lib;
svn  --accept $CONFLICT_ACCEPT up target;
svn  --accept $CONFLICT_ACCEPT up api;

if [ -d internal ];then
svn  --accept $CONFLICT_ACCEPT up internal;
fi

chmod +x target/script/install-nio.sh;
cp -f target/script/restart-nio.sh $DEST;
chmod +x $DEST/restart-nio.sh;

echo "APPLICATION LOCATION:$DEST";


SRCDIR=`pwd`;
if [[ ! -d $SRCDIR/target && ! -d $SRCDIR/lib ]];then
echo "Source directory $SRCDIR is not a valid source dir!";
exit 7
fi


#Clean all jar and classes
svn up $DEST/lib/
if [ -z $2 ];then
CLEAR_TYPE=$INSTALL_TYPE;
else
INSTALL_TYPE=$2;
fi
if [ $INSTALL_TYPE -eq 1 ];then
rm -f $DEST/lib/eis-*.jar;
echo "Clear old class file and jar files...";
fi

# Commit local conflict to server, default is accept both conflict from server
if [ -z $3 ];then
CONFLICT_ACCEPT="theirs-full";
elif [ $3 -eq 1 ];then
CONFLICT_ACCEPT="mine-full";
echo "WARNING: conflict mode changed to LOCAL first! local changes will be submitted to svn server!";
fi
make_dir;

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
cp strict/eis-common-cloud.jar $DEST/lib/


cd $DEST
svn -q up;

svn -q up $DEST/classes;
svn -q up $DEST/classes/sconfig;
svn -q up $DEST/classes/pconfig;



cd $SRCDIR;


