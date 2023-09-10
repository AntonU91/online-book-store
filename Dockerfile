# Builder stage
FROM openjdk:17-jdk-alpine as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p target
COPY ./target application/target
ENTRYPOINT ["java","-jar","/app.jar"]

# Final stage
FROM openjdk:17-jdk-alpine
WORKDIR application
COPY --from=builder application/target ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8080






