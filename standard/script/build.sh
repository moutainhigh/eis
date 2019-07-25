#!/bin/sh
cd /root/changxiu-source

export LC_ALL=en_US
echo "Checkout boss source"
cd ./cms-3.0/
svn up --quiet
echo "Building boss source"
ant -q

cd ..
echo "Packing boss binary to changxiu-boss-3.jar"
jar cf changxiu-boss-3.jar -C cms-3.0/WebContent/WEB-INF/classes/ net/

echo "Checkout front source"
cd ./web-3.0/
svn up --quiet

echo "Coping boss binary to front lib"
alias cp='cp'
cp ../changxiu-boss-3.jar ./lib/
alias cp='cp -i'

echo "Building front source"
ant -q


cd ..
echo "Packing front binary to changxiu-front-3.jar"
jar cf changxiu-front-3.jar -C web-3.0/WebContent/WEB-INF/classes/ net/

export LC_ALL=zh_CN
