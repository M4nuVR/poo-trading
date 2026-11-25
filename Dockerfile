# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn --batch-mode --no-transfer-progress package

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN groupadd --gid 10001 trading \
    && useradd --uid 10001 --gid trading --create-home trading \
    && install -d -o trading -g trading /app/data
COPY --from=build /build/target/*.jar /app/poo-trading.jar
USER trading
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/poo-trading.jar"]
