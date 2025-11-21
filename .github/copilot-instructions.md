# Copilot Instructions for Team

## Frameworks
- Use Spring Boot 3, Java 17.
- Use KafkaTemplate for producing events.
- Use Camunda External Task Client for workflow integration.
- Use memcached-spring-boot-starter for caching.

## Coding Guidelines
- Always separate controller, service, and repository layers.
- Use DTO mapping with ModelMapper.
- Logging using Logback with SLF4J.
- No business logic inside controllers.
- Add comment with  username as Dinesh  as the first line for any new comment suggested or created by Copilot.

## Unit Tests
- Use JUnit 5 + Mockito
- Always mock external services
- Use @WebMvcTest for controllers
- Use @DataJpaTest for repositories

## Project-Specific Rules
- All Kafka messages must contain correlationId.
- For workflows, use BPMN variable naming convention: snake_case.
- Use MapStruct for conversion wherever possible.
