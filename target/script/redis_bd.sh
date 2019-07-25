#!/bin/sh
echo "redis-cli -a $1  keys $2|xargs redis-cli -a $1 del {} \;"
