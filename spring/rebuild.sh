#!/bin/bash
docker stop myapp 2>/dev/null
docker rm myapp 2>/dev/null
docker build -t myapp .

# 确保网络存在
docker network inspect mynetwork >/dev/null 2>&1 || docker network create mynetwork

# 确保mysql在网络中
docker network connect mynetwork mysql 2>/dev/null

# 运行容器（传入环境变量覆盖数据库地址）
docker run -d \
  -p 8080:8080 \
  --name myapp \
  --network mynetwork \
  -e DB_URL=jdbc:mariadb://mysql:3306/quiz_system?useSSL=false \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=123456 \
  myapp
