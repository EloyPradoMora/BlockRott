FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY .gradle .gradle
COPY gradle gradle
COPY build.gradle.kts .
COPY docker-settings.gradle.kts settings.gradle.kts
COPY src src

RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080

ENTRYPOINT java -jar app.jar