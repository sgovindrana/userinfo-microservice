FROM openjdk:21-jdk-slim AS builder
WORKDIR /opt
COPY target/*jar  /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar