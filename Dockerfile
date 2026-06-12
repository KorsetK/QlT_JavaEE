FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# 阿里云Maven镜像加速
RUN mkdir -p /root/.m2 && \
    echo '<?xml version="1.0" encoding="UTF-8"?>\n\
<settings>\n\
  <mirrors>\n\
    <mirror>\n\
      <id>aliyunmaven</id>\n\
      <name>阿里云公共仓库</name>\n\
      <url>https://maven.aliyun.com/repository/public</url>\n\
      <mirrorOf>central</mirrorOf>\n\
    </mirror>\n\
  </mirrors>\n\
</settings>' > /root/.m2/settings.xml

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
