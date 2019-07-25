#!/bin/sh
baseName=`basename $1 .class` 
dirName=`dirname $1`
cryptFile=$dirName/$baseName.eisc
java -cp ../target/*:../cloud/dep/*:../lib/* com.maicard.cloud.utils.CryptClassLoader $1
echo "Crypt file $1 to $cryptFile"
mv $1 $cryptFile
