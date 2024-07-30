# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Copy the application JAR file into the container at /app
COPY build/libs/user-ms-0.0.1-SNAPSHOT.jar user-ms.jar

# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "user-ms.jar"]