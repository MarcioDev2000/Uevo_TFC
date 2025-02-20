FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
RUN apt-get install maven -y

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

EXPOSE 8082


COPY --from=build /target/EvoluTCC-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

# Rodar a aplicação
ENTRYPOINT [ "java", "-jar", "app.jar" ]