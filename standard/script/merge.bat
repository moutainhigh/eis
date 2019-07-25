@echo off
svn ci -m ""
svn up
svn delete svn://svn.maicard.com/eis/BRANCHES/v4 -m ""
svn copy svn://svn.maicard.com/eis/CURRENT  svn://svn.maicard.com/eis/BRANCHES/v4 -m ""