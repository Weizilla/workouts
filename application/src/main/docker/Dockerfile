FROM openjdk:8-jre-alpine
MAINTAINER Wei Yang <wei@weizilla.com>

EXPOSE 80
VOLUME /data
ADD config.yml config.yml
ADD workouts-application.jar workouts-application.jar
CMD ["java", "-jar", "-Done-jar.silent=true", "workouts-application.jar", "server", "config.yml"]
