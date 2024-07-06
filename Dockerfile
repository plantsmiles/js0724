FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y postgresql-client

WORKDIR /app

COPY build/libs/*.jar app.jar
COPY wait-for-it.sh .

RUN chmod +x wait-for-it.sh

ENTRYPOINT ["./wait-for-it.sh", "postgres", "java", "-jar", "app.jar"]