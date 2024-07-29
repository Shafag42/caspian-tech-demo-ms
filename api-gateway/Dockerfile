# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Copy the application JAR file into the container at /app
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar

# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]