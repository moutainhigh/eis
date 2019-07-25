#!/bin/sh
. /etc/profile
BACKUP_DEST=~/backup/chaoka
mkdir -p $BACKUP_DEST


BACKUP_FILE=$BACKUP_DEST/chaoka_mysql_`date +%Y%m%d%H`.sql
mysqldump -S /tmp/mysql2.sock -uchaoka -pv1ctYL1 eis-v4-chaoka > $BACKUP_FILE
gzip -9 -q -f $BACKUP_FILE

SRC="/var/chaoka.cn/upload/productFile"

BACKUP_FILE=$BACKUP_DEST/chaoka_image_`date +%Y%m%d%H`.tar
tar cf $BACKUP_FILE $SRC
gzip -9 -q -f $BACKUP_FILE
