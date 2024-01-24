
#
# AUTHOR: William A. Morris
# CREATION_DATE: 2024-01-19
# PURPOSE:
#   create Docker Container from generated application artifacts
#

# Container will be run on x86-64 Linux Platform
# Pull Amazon Corretto flavor of Java JDK, version 17 (same as project)
FROM --platform=x86-64 amazoncorretto:17-alpine-jdk

# ENV SYS_VAR toExposeToContainer
ENV MYSQL_PATH=host.docker.internal
ENV MYSQL_DATABASE_USERNAME=eecs447
ENV MYSQL_DATABASE_PASSWORD=password

# create and move to directory /app to store artifacts
WORKDIR /app

# copy Java ARchieve (Jar) into app folder
COPY target/eecs447-project-service.jar eecs447-project-service.jar

# set entrypoint (command which will run when container is started)
ENTRYPOINT ["java","-jar","/app/eecs447-project-service.jar"]

# expose appropriate API port
EXPOSE 8080
