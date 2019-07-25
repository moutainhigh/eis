#!/bin/sh
MARJOR=4
cd $1
REV=`svn info|grep "^Revision"|sed s/Revision:\ //|awk '{printf("%05s\n",$0)}'`
MINOR=`echo $REV|awk '{print substr($0,1,1)}'`
BUILD=`echo $REV|awk '{print substr($0,2)}'`
REV=$MARJOR.$MINOR.$BUILD
unset MINOR
unset BUILD
