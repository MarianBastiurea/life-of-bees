# Stage 1 – build cu Maven + JDK 23
FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app

# Copiem tot proiectul
COPY . .

# Construim backend-ul
RUN mvn clean package -DskipTests


# Stage 2 – imagine finală doar cu JDK 23
FROM eclipse-temurin:23-jdk

WORKDIR /app

# Copiem jar-ul generat
COPY --from=build /app/target/life-of-bees-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]