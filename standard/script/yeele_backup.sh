#!/bin/sh
. /etc/profile
BACKUP_DEST=~/backup/yeele
MYSQL_SOCK=/var/lib/mysql2/mysql2.sock

mkdir -p $BACKUP_DEST


BACKUP_FILE=$BACKUP_DEST/yeele_mysql_`date +%Y%m%d`.sql
mysqldump -S $MYSQL_SOCK -uyeele -pv1ctYL1 eis_v4_yeele > $BACKUP_FILE
gzip -9 -q -f $BACKUP_FILE

SRC="/var/yeele.cn/upload/ /var/yeele.cn/tomcat-boss-7.0.32/webapps/ewebeditor/uploadfile"

BACKUP_FILE=$BACKUP_DEST/yeele_image_`date +%Y%m%d`.tar
tar cf $BACKUP_FILE $SRC
gzip -9 -q -f $BACKUP_FILE
