#!/bin/sh
cd common;
svn up; ant clean; ant jar;cd ..
cd standard;
svn up; ant clean; ant jar;cd ..
cd common-cloud
svn up; ant clean; ant jar;cd ..
