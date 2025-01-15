FROM eclipse-temurin:23
WORKDIR /app
COPY target/pets-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
