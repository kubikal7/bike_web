# Start from OpenJDK 21 base image
FROM openjdk:21-jdk

# Ustaw katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj plik wait-for-it.sh i nadaj mu prawa do wykonania
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Skopiuj JAR aplikacji do kontenera (dostosuj ścieżkę jeśli trzeba)
COPY target/BikeWeb-0.0.1-SNAPSHOT.jar /app/BikeWeb-0.0.1-SNAPSHOT.jar

# Komenda uruchamiająca aplikację, ale przez wait-for-it.sh czekając na MySQL
CMD ["/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "/app/BikeWeb-0.0.1-SNAPSHOT.jar"]

