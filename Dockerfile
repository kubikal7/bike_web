FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=builder /build/target/BikeWeb-0.0.1-SNAPSHOT.jar /app/BikeWeb-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/BikeWeb-0.0.1-SNAPSHOT.jar"]


