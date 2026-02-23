FROM maven:3.9.9-eclipse-temurin-21-jammy AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN useradd -u 10001 -r -g root app \
    && chown -R app:root /app

USER app

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
