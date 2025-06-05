# Stage 1: Build the application using Gradle
FROM gradle:8.8.0-jdk21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle build files AND the wrapper script
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src

# Grant execution permissions to gradlew
RUN chmod +x ./gradlew

# Build the application JAR file
# Use --no-daemon to avoid issues in some CI/CD environments
RUN ./gradlew build --no-daemon -x test

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre-jammy

# Create a non-root user and group
RUN groupadd --gid 1001 appgroup && \
    useradd --uid 1001 --gid appgroup --create-home --shell /bin/bash appuser

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Set environment variable for the database URL (example, can be overridden)
# This fulfills the requirement of having at least one ENV variable
ENV DB_URL=jdbc:postgresql://postgres-db:5432/extreme_events_db

# Expose the application port (default 8080, can be overridden by SERVER_PORT env var)
EXPOSE 8080

# Change ownership of the app directory and JAR file to the non-root user
# Ensure the user exists before changing ownership
RUN chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser

# Define the entry point for running the application
ENTRYPOINT ["java", "-jar", "app.jar"]

