#!/bin/sh

clean(){
	cd standard
	ant clean

	cd ../partner
	ant clean

	cd ../front
	ant clean

	cd ../boss
	ant clean

	if [ -d ../transaction-system ];then
		cd ../transaction-system
		ant clean
	fi
}
if [ -d ~/svn/v4/CURRENT ];then
	cd  ~/svn/v4/CURRENT
	clean;
fi

if [ -d ~/svn/v4/BRANCHES/v4 ];then
	cd  ~/svn/v4/BRANCHES/v4
	clean;
fi