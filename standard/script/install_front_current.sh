#!/bin/sh

COPY_STANDARD_CONFIG=true;
SRCDIR=~/svn/v4/CURRENT
cd $SRCDIR


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
DEST_APP=$DEST/webapps/v4-front

cd standard
svn up
ant -q jar
if [ -d ../front ];then
	cd ../front
	svn up
fi

if [ -d ../gallery ];then
cd ../gallery
	svn up
	rm -f *.jar
	ant -q jar
	cp eis-v4-current-gallery.jar $DEST_APP/WEB-INF/lib/
	cp src/bean-v4-gallery.xml $DEST_APP/WEB-INF/classes/
else 
	if [ -d ../front ];then
		rm  ../front/src/com/maicard/front/controller/jrsj/processor/JrsjZazhiNodeProcessorImpl.java
		rm ../front/src/com/maicard/front/controller/jrsj/processor/JrsjCartNodeProcessorImpl.java
		rm ../front/src/com/maicard/front/api/DownloadController.java
	fi
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

if [ -d ../ec ];then
	cd ../ec
	svn up
	rm -f *.jar
	ant -q jar
	cp eis-v4-current-ec.jar $DEST_APP/WEB-INF/lib/
fi

if [ -d ../o2o ];then
	cd ../o2o
	svn up
	rm -f *.jar
	ant -q jar
	cp eis-v4-current-o2o.jar $DEST_APP/WEB-INF/lib/
fi

if [ -d ../game ];then
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

if [ -d ../transaction-system ];then
	cd ../transaction-system
	svn up
	rm -f *.jar
	ant -q jar
	cp eis-v4-current-ts.jar $DEST_APP/WEB-INF/lib/
fi

if [ -d ../front ];then
	cd ../front
	ant -q jar
	cp eis-v4-current-front.jar $DEST_APP/WEB-INF/lib/
fi


cd ..
# cp standard/lib/* $DEST_APP/WEB-INF/lib/
cp standard/lib/mysql-connector-*.jar $DEST/lib/
cp standard/eis-v4-current-standard.jar $DEST_APP/WEB-INF/lib/
cp standard/src/*.xml $DEST_APP/WEB-INF/classes/
cp standard/src/*.xsd $DEST_APP/WEB-INF/classes/
cp standard/src/uuwise.properties $DEST_APP/WEB-INF/classes/
if [ ! -d $DEST_APP/WEB-INF/classes/config ];then
	mkdir -p $DEST_APP/WEB-INF/classes/config
fi
if $COPY_STANDARD_CONFIG;then
	echo "Copy standard config xml...";
	rsync -au standard/config/my* $DEST_APP/WEB-INF/classes/config/ --exclude .svn --exclude .svn/*
fi
cd $DEST_APP
svn up

cd $DEST_APP/WEB-INF/lib/
svn up


rm -f $DEST_APP/WEB-INF/lib/servlet-api.jar
rm -f $DEST_APP/WEB-INF/lib/mysql-connector-*.jar
rm -f $DEST_APP/WEB-INF/lib/tomcat-jdbc.jar

export LC_ALL=zh_CN.GBK
