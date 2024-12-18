#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy-jar.sh"
	exit 1
}
# copy jar
echo "begin copy mall-admin "
cp ../mall-admin/target/mall-admin.jar ./backend/mall-admin.jar
