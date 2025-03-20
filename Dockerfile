# Step 1: Use Maven official image to build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and dependencies first (cache optimization)
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Copy the source code and build the project
COPY src src
RUN ./mvnw clean package -DskipTests

# Step 2: Use lightweight JDK image for runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the generated JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
