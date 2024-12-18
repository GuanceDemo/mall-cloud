#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy-jar.sh"
	exit 1
}

SERVICES=("gateway" "admin" "auth" "portal" "search")

# 定义复制文件的函数
copy_service_jar() {
  local service_name=$1
  cp "../mall-$service_name/target/mall-$service_name.jar" "./backend/mall-$service_name/app.jar"
}

# 复制所有服务的jar文件
for service in "${SERVICES[@]}"; do
  # copy jar
  echo "begin copy mall-$service "
  copy_service_jar $service
done

