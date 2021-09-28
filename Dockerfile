# syntax=docker/dockerfile:1
FROM openjdk:8-jre
WORKDIR /usr/local
COPY ./target/ToolSetUserCenter.jar /usr/local/ToolSetUserCenter.jar
ENTRYPOINT ["java", "-jar", "/user/local/ToolSetUserCenter.jar"]
