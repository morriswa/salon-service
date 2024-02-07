
#
# AUTHOR: William A. Morris
# CREATION_DATE: 2024-01-19
# PURPOSE:
#   create Docker Container from generated application artifacts
#   to build container in local repository, first run mvn package to generate required artifacts
#

# Container will be run on x86-64 Linux Platform
# Pull Amazon Corretto flavor of Java JDK, version 17 (same as project)
FROM --platform=x86-64 amazoncorretto:17-alpine-jdk

# ENV SYS_VAR=default_value
ENV MYSQL_HOSTNAME=host.docker.internal
ENV SERVER_CACHE='/cache'

# create and move to directory /app to store artifacts
WORKDIR /app

# copy Java ARchieve (Jar) into /app folder
COPY target/salon-service.jar salon-service.jar

# create file cache
RUN mkdir /cache

# set entrypoint (command which will run when container is started)
ENTRYPOINT ["java","-jar","/app/salon-service.jar"]

# expose appropriate API port
EXPOSE 8080
