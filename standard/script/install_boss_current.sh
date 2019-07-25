#!/bin/sh

COPY_STANDARD_CONFIG=true;

BASE_PATH=~/svn/v4/CURRENT
cd $BASE_PATH

if [ ! $1  ] 
then
	echo "Usage: script install destination..."
	exit 1
fi
export LC_ALL=en_US.UTF-8
DEST=$1
if [ ! -d $DEST ]
then
	echo "Destination $DEST not exist.";
	exit 2
fi
DEST_APP=$DEST/webapps/v4-boss

cd standard
rm -f *.jar
svn up
ant -q jar
if [ ! -e eis-v4-current-standard.jar ];then
	echo "Standard module ERROR, jar package NOT found!!!"
	exit 3
fi
cp eis-v4-current-standard.jar $DEST_APP/WEB-INF/lib/

. $BASE_PATH/standard/script/update_version.sh `pwd`
echo $REV
sed /systemVersion/d $DEST_APP/WEB-INF/classes/server-special-v4.properties > ~/rev
echo  "systemVersion=$REV" >> ~/rev
cp ~/rev $DEST_APP/WEB-INF/classes/server-special-v4.properties

cd ../boss
rm -f *.jar
svn up
ant -q jar
if [ ! -e eis-v4-current-boss.jar ];then
	echo "BOSS module ERROR, jar package NOT found!!!"
	exit 3
fi
cp eis-v4-current-boss.jar $DEST_APP/WEB-INF/lib/

if [ -d ../gallery ];then
cd ../gallery
svn up
rm -f *.jar
ant -q jar
if [ ! -e eis-v4-current-gallery.jar ];then
	echo "Gallery module ERROR, jar package NOT found!!!"
	exit 3
fi
cp eis-v4-current-gallery.jar $DEST_APP/WEB-INF/lib/
fi

if [ -d ../ec ];then
	cd ../ec
	echo "Found EC module, installing..."
	svn up
	rm -f *.jar
	ant jar
	if [ ! -e eis-v4-current-ec.jar ];then
		echo "EC module ERROR, jar package NOT found!!!"
		exit 3
	fi
	cp eis-v4-current-ec.jar $DEST_APP/WEB-INF/lib/
fi

if [ -d ../wpt ];then
	cd ../wpt
	svn up
	rm -f *.jar
	ant -q jar
	if [ ! -e eis-wpt.jar ];then
		echo "WPT module ERROR, jar package NOT found!!!"
		exit 3
	fi
	cp eis-wpt.jar $DEST_APP/WEB-INF/lib/
	if [ -d WebContent/config/ ];then
		mkdir -p $DEST_APP/WEB-INF/classes/config/
		cp WebContent/config/*.xml $DEST_APP/WEB-INF/classes/config/
		COPY_STANDARD_CONFIG=false;
	fi
fi

if [ -d ../game ];then
	echo "Found GAME module, installing..."
	cd ../game
	svn up
	rm -f *.jar
	ant -q clean
	ant -q jar
	if [ ! -e eis-game.jar ];then
		echo "Game module ERROR, jar package NOT found!!!"
		exit 3
	fi
	cp eis-game.jar $DEST_APP/WEB-INF/lib/
	if [ -d WebContent/config/ ];then
		mkdir -p $DEST_APP/WEB-INF/classes/config/
		cp WebContent/config/*.xml $DEST_APP/WEB-INF/classes/config/
		COPY_STANDARD_CONFIG=false;
	fi
fi

if [ -d ../o2o ];then
	echo "Found O2O module, installing..."
	cd ../o2o
	svn up
	rm -f *.jar
	ant -q clean
	ant -q jar
	if [ ! -e eis-v4-current-o2o.jar ];then
		echo "O2O module ERROR, jar package NOT found!!!"
		exit 3
	fi
	cp eis-v4-current-o2o.jar $DEST_APP/WEB-INF/lib/
fi

if [ -d ../transaction-system ];then
	echo "Found TS module, installing..."
	cd ../transaction-system
	rm -f *.jar
	svn up
	ant -q clean
	ant -q jar
	if [ ! -e eis-v4-current-ts.jar ];then
		echo "Transaction module ERROR, jar package NOT found!!!"
		exit 3
	fi
	cp eis-v4-current-ts.jar $DEST_APP/WEB-INF/lib/
fi

cd ..

# cp standard/lib/* $DEST_APP/WEB-INF/lib/
cp standard/lib/mysql-connector-*.jar $DEST/lib/
cp standard/src/*.xml $DEST_APP/WEB-INF/classes/
cp standard/src/*.xsd $DEST_APP/WEB-INF/classes/
if [ ! -d $DEST_APP/WEB-INF/classes/config ];then
	mkdir -p $DEST_APP/WEB-INF/classes/config
fi
if $COPY_STANDARD_CONFIG;then
	echo "Copy standard config xml...";
	rsync -au standard/config/my* $DEST_APP/WEB-INF/classes/config/ --exclude .svn --exclude .svn/*
fi
cd $DEST_APP
find ./ -maxdepth 1  -type d |grep -v .svn |xargs svn -q up
cd $DEST_APP/WEB-INF
find ./ -maxdepth 1 -type d |grep -v .svn |xargs svn -q up


rm -f $DEST_APP/WEB-INF/lib/servlet-api.jar
rm -f $DEST_APP/WEB-INF/lib/mysql-connector-*.jar
rm -f $DEST_APP/WEB-INF/lib/tomcat-jdbc.jar


export LC_ALL=zh_CN.GBK
