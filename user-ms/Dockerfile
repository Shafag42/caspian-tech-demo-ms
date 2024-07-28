# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY build/libs/user-ms-0.0.1-SNAPSHOT.jar /app/user-ms.jar

# Copy the application.yml files into the container at /app
COPY src/main/resources/application.yml /app/application.yml

# Expose port 9080
EXPOSE 9080

# Specify the command to run your application
CMD ["java", "-jar", "user-ms.jar"]