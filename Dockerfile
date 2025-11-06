FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/localiza-0.0.1-SNAPSHOT.jar /app/localiza.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/localiza.jar"]