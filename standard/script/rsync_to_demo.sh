#!/bin/sh
. /etc/profile
rsync -auvzc /var/yeele.cn/upload 124.40.117.83::yeele_upload
