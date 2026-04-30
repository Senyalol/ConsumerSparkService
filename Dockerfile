FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

#FROM eclipse-temurin:23-jre-alpine
FROM eclipse-temurin:23-jdk

WORKDIR /web

COPY --from=build /app/target/*.jar web.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "web.jar"]