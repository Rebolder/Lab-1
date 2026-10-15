FROM eclipse-temurin:latest
LABEL authors="artem"
VOLUME tmp/
COPY target/Lab_1-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]