#!/bin/bash
docker stop myapp 2>/dev/null
docker rm myapp 2>/dev/null
docker build -t myapp .

docker network inspect mynetwork >/dev/null 2>&1 || docker network create mynetwork
docker network connect mynetwork mysql 2>/dev/null

docker run -d -p 8080:8080 --name myapp --network mynetwork \
  -e DB_URL=jdbc:mariadb://mysql:3306/quiz_system?useSSL=false \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=123456 \
  myapp
