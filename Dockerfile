FROM openjdk:23-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the application JAR file into the /app directory
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]