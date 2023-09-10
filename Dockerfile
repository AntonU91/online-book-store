# Builder stage
FROM openjdk:17-jdk-alpine as builder
WORKDIR /application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# Final stage
FROM openjdk:17-jdk-alpine
WORKDIR /application
COPY --from=builder /application/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080






