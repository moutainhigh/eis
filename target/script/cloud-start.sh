export LC_ALL=zh_CN.GBK
CPATH=`pwd`
cd ~/svn/DEVELOP/target
cp eis-nio.jar eis-cloud-server.jar eis-standard.jar $CPATH/lib
cp ~/svn/DEVELOP/strict/eis-common-cloud.jar $CPATH/lib/
cd $CPATH
cp ~/svn/DEVELOP/cloud-server/WebContent/cloud-server/context-cloud-server.xml classes/
ps -ef|grep $CPATH|grep java|awk '{print $2}'|xargs kill
rm -f nohup.out
nohup java -cp $CPATH/classes/:lib/* com.maicard.nio.netty.Bootstrap context-cloud-server.xml  &
