# Build stage
FROM maven:3.9-amazoncorretto-21 as build-stage

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source files
COPY src/ ./src/

# Build the application
RUN mvn package -DskipTests
RUN ls -la target/

# Runtime stage
FROM amazoncorretto:21-alpine as production-stage

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build-stage /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set JVM options (adjust as needed)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]