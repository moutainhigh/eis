#!/bin/sh

export LC_ALL=en_US.UTF-8
BASEDIR=~/svn/v4/CURRENT
DEST=$1
if [ ! $DEST  ];then
        echo -e "Usage: $0 install_destination operate\nor $0 operate\n"
        echo -e "Example: $0 /var/yeele.cn/ install\n"
        echo -e "\n$0 clean\n=> Remove all old class file."
        echo -e "\n$0 build\n=>Compile all class file."
        echo -e "\n$0 /var/test.com copy\n=>Copy all class file to destination and update file in destination."
        echo -e "\n$0 /var/test.com install\n=>Compile all class file and copy to destination and update file in destination."
        echo -e "\n$0 /var/test.com\n=>Remove all old classes files, recompile  and copy to destination and update file in destination."
        exit 1
fi
clean() {
echo ">>>>>>>>>>> Clean all module... >>>>>>>>>>"
if [ -d $BASEDIR/standard ];then
        cd $BASEDIR/standard
        rm -f *.jar
        ant clean 1>/dev/null
fi

if [ -d $BASEDIR/transaction-system ];then
        cd $BASEDIR/transaction-system
        rm -f *.jar
        ant clean 1>/dev/null
fi

if [ -d $BASEDIR/front ];then
        cd $BASEDIR/front
        rm -f *.jar
        ant clean 1>/dev/null
fi

if [ -d $BASEDIR/boss ];then
        cd $BASEDIR/boss
        rm -f *.jar
        ant clean 1>/dev/null
fi

if [ -d $BASEDIR/partner ];then
        cd $BASEDIR/partner
        rm -f *.jar
        ant clean 1>/dev/null
fi
}

build() {
echo ">>>>>>>>>>> Build all module... >>>>>>>>>>"
if [ -d $BASEDIR/standard ];then
        cd $BASEDIR/standard
        rm -f *.jar
        svn -q up
        ant jar 1>/dev/null
fi

if [ -d $BASEDIR/transaction-system ];then
        cd $BASEDIR/transaction-system
        rm -f *.jar
        svn -q up
        ant jar 1>/dev/null
fi

if [ -d $BASEDIR/front ];then
        cd $BASEDIR/front
        rm -f *.jar
        svn -q up
        ant jar 1>/dev/null
fi

if [ -d $BASEDIR/boss ];then
        cd $BASEDIR/boss
        rm -f *.jar
        svn -q up
        ant jar 1>/dev/null
fi

if [ -d $BASEDIR/partner ];then
        cd $BASEDIR/partner
        rm -f *.jar
        svn -q up
        ant jar 1>/dev/null
fi
}

copy() {
echo ">>>>>>>>>>> Copy and update all module... >>>>>>>>>>"
if [ ! $DEST  ];then
        exit 1
fi
if [ ! -d $DEST ];then
        echo "Destination $DEST not exist.";
        exit 2
fi
for webapp in `find $DEST -type d -name v4-backend`;do
        echo "Install [BACKEND] to $webapp";
        cp $BASEDIR/standard/src/*.xml $webapp/WEB-INF/classes/
        cp $BASEDIR/standard/src/*.xsd $webapp/WEB-INF/classes/

        cd $webapp
        svn -q up
        cd WEB-INF/lib
        svn -q up
        rm -f $webapp/WEB-INF/lib/servlet-api.jar
        rm -f $webapp/WEB-INF/lib/tomcat-jdbc.jar
        rm -f $webapp/WEB-INF/lib/mysql-connector-*.jar
        rm -f $webapp/WEB-INF/lib/eis-v4-current*.jar

        cp $BASEDIR/standard/eis-v4-current-standard.jar $webapp/WEB-INF/lib/
        cp $BASEDIR/transaction-system/eis-v4-current-ts.jar $webapp/WEB-INF/lib/ 2>/dev/null

done;

for webapp in `find $DEST -type d -name v4-boss`;do
        echo "Install [BOSS] to $webapp";
        cp $BASEDIR/standard/src/*.xml $webapp/WEB-INF/classes/
        cp $BASEDIR/standard/src/*.xsd $webapp/WEB-INF/classes/

        cd $webapp
        svn -q up
        cd WEB-INF/lib
        svn -q up
        rm -f $webapp/WEB-INF/lib/servlet-api.jar
        rm -f $webapp/WEB-INF/lib/tomcat-jdbc.jar
        rm -f $webapp/WEB-INF/lib/mysql-connector-*.jar
        rm -f $webapp/WEB-INF/lib/eis-v4-current*.jar

        cp $BASEDIR/standard/eis-v4-current-standard.jar $webapp/WEB-INF/lib/
        cp $BASEDIR/boss/eis-v4-current-boss.jar $webapp/WEB-INF/lib/ 
done;


for webapp in `find $DEST -type d -name v4-partner`;do
        echo "Install [PARTNER] to $webapp";
        cp $BASEDIR/standard/src/*.xml $webapp/WEB-INF/classes/
        cp $BASEDIR/standard/src/*.xsd $webapp/WEB-INF/classes/

        cd $webapp
        svn -q up
        cd WEB-INF/lib
        svn -q up
        rm -f $webapp/WEB-INF/lib/servlet-api.jar
        rm -f $webapp/WEB-INF/lib/tomcat-jdbc.jar
        rm -f $webapp/WEB-INF/lib/mysql-connector-*.jar
        rm -f $webapp/WEB-INF/lib/eis-v4-current*.jar

        cp $BASEDIR/standard/eis-v4-current-standard.jar $webapp/WEB-INF/lib/
        cp $BASEDIR/partner/eis-v4-current-partner.jar $webapp/WEB-INF/lib/ 
done;

for webapp in `find $DEST -type d -name v4-front`;do
        echo "Install [FRONT] to $webapp";
        cp $BASEDIR/standard/src/*.xml $webapp/WEB-INF/classes/
        cp $BASEDIR/standard/src/*.xsd $webapp/WEB-INF/classes/

        cd $webapp
        svn -q up
        cd WEB-INF/lib
        svn -q up
        rm -f $webapp/WEB-INF/lib/servlet-api.jar
        rm -f $webapp/WEB-INF/lib/tomcat-jdbc.jar
        rm -f $webapp/WEB-INF/lib/mysql-connector-*.jar
        rm -f $webapp/WEB-INF/lib/eis-v4-current*.jar

        cp $BASEDIR/standard/eis-v4-current-standard.jar $webapp/WEB-INF/lib/
        cp $BASEDIR/front/eis-v4-current-front.jar $webapp/WEB-INF/lib/ 
done;
}
case "$1" in
    clean)
        clean
        exit
        ;;
    build)
        build
        exit
        ;;
    *)
esac



case "$2" in
    clean)
        clean
        exit
        ;;
    build)
        build
        exit
        ;;
    copy)
        copy
        exit
        ;;
    install)
        build
        copy
        exit
        ;;
    *)
        clean
        build
        copy
esac

