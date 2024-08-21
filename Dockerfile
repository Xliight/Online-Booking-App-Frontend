FROM openjdk:21
WORKDIR /app
COPY target/security-0.0.1-SNAPSHOT.jar /app/booking.jar
EXPOSE 8080
ENTRYPOINT  ["java", "-jar", "booking.jar"]
