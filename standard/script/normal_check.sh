#!/bin/sh
. /etc/profile
echo  ">>>>>>>>>>>>>>>>>>>> `date +'%Y-%m-%d %H:%M:%S'` >>>>>>>>>>>>>>>>"
for i in 8100 8101 8103 8104 8105 80; do
	java -classpath /var/yeele.cn/eis-current-management.jar com.maicard.management.monitor.YeeleRegisterMonitor www.yeele.cn $i  2>/dev/null
done

echo -n "Activemq memory used:"
ps auxwww|grep activemq|grep java|awk '{printf("%.2f%, %sM\n", $6/$5*100, $6/1024)}'

echo -n "Nginx "
curl 'http://127.0.0.1/nginx-status' 2>&1| grep "Active connections"

echo -n "System memory used:"
free|grep Mem|awk '{printf("%.2f%\n", $3/$2*100)}'
echo -n "Max disk used:" 
df |sed '1d;'|awk '{print $5}'|sort -nr|head -n1
uptime
echo  "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
echo ""
