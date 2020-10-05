# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk11:alpine-jre

# Add Maintainer Info
LABEL maintainer="ravigupta.se92@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8086 available to the world outside this container
EXPOSE 8086

# The application's jar file
ARG JAR_FILE=target/assignment-0.0.1-SNAPSHOT.jar

# cp target/spring-boot-web.jar /opt/app/app.jar
ADD ${JAR_FILE} assignment.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./assignment.jar"]
