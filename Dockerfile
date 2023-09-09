# Builder stage
FROM openjdk:17.0.2-jdk-slim-buster as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Final stage
FROM openjdk:17-jdk-alpine
WORKDIR application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8080