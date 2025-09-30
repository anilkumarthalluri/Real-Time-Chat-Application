# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Copy the project files to the container
WORKDIR /app
COPY . .

# Build the project and create the executable JAR
RUN mvn clean package -DskipTests

# Stage 2: Create the final, smaller image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the executable JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Add a delay to the entrypoint to wait for the database to be ready
ENTRYPOINT ["sh", "-c", "sleep 10; java -jar app.jar"]
