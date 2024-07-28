# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar

# Copy the env.properties and application.yml files into the container at /app
COPY src/main/resources/application.yml /app/application.yml

# Expose port 8060
EXPOSE 9090

# Specify the command to run your application
CMD ["java", "-jar", "api-gateway.jar"]