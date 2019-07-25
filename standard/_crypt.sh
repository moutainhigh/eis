#!/bin/sh
baseName=`basename $1 .class` 
dirName=`dirname $1`
cryptFile=$dirName/$baseName.eisc
cloud-crypt $1 enc
mv $1 $cryptFile
