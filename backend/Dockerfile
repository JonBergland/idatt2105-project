# Build stage
FROM maven:3.9-amazoncorretto-21

# Set working directory
WORKDIR /src

# Expose port
EXPOSE 8080

# Set JVM options
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Default command uses Spring Boot's dev mode
CMD ["./mvnw", "spring-boot:run", "-DskipTests"]