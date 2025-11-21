<!-- Dinesh -->
# Java Interview - Setup

A sample project to verify local development environment setup for a live coding session. This repository contains a small Spring Boot application (Java 17, Maven) with example controller, service, repository layers and unit tests used as a baseline for interview exercises.

## Summary
A minimal, ready-to-run Java Spring Boot project used to validate a candidate's local environment and to run basic coding exercises during interviews. It includes examples of controllers, services, repositories, and unit tests.

## Prerequisites
- Java 17+
- Maven
- Git
- An IDE (IntelliJ IDEA, VS Code, etc.)

## Setup Steps
1. Clone this repository:

```bash
git clone <repo-url>
cd dinesh-learn-setup
```

2. Open the project in your preferred IDE.

3. Build the project using Maven:

```bash
./mvnw clean install
# or
mvn clean install
```

4. Run tests:

```bash
./mvnw test
# or
mvn test
```

## Run the Application
To run the Spring Boot application locally:

```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

The application will start on the port configured in `src/main/resources/application.properties` (default 8080).

## Running from the packaged JAR

```bash
./mvnw package
java -jar target/java-backend-interview-0.0.1-SNAPSHOT.jar
```

## Project Structure (high level)
```
src/
  main/
    java/
      com/real/interview/
        controller/        # REST controllers
        service/           # Service layer interfaces & implementations
        repository/        # Spring Data JPA repositories
        entity/            # JPA entities
        exception/         # Custom exceptions & handlers
  test/
    java/                # Unit tests (JUnit 5 + Mockito)
```

## Testing Guidelines
- Unit tests use JUnit 5 and Mockito.
- Controller tests use `@WebMvcTest`.
- Repository tests use `@DataJpaTest`.

## Troubleshooting
- If you see Java version issues, ensure `java -version` shows Java 17+ and your `JAVA_HOME` is set correctly.
- If Maven wrapper fails, install Maven and run `mvn` commands directly.

## Notes for Interviewers/Candidates
- The project is intentionally small and focuses on verifying that candidates can:
  - Import and build a Maven project
  - Run unit tests
  - Start a Spring Boot app locally
- Use the `target/` directory for built artifacts and test reports.

## Tags
- java
- spring-boot
- maven
- interview
- junit5

## Contact
For questions about this repository, contact the repository owner or the interviewer.

