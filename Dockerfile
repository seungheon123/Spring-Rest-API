FROM openjdk:11-jdk
LABEL maintainer="seungheon.bib.2012@gmail.com"
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-jar","./docker-springboot.jar"]