
# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Quarkusaurus is a Quarkus-based microservice example built with Kotlin and Gradle. It demonstrates hexagonal architecture (ports and adapters) for a simple task management API.

## Build and Development Commands

### Development Mode
```bash
./gradlew quarkusDev
```
Runs with hot reload. Dev UI available at http://localhost:8080/q/dev/

### Build
```bash
./gradlew build
```
Runs tests and creates `quarkus-run.jar` in `build/quarkus-app/`

### Run Tests
```bash
./gradlew test
```
Note: Tests depend on `quarkusBuild` task (configured in build.gradle.kts:34)

### Run Single Test
```bash
./gradlew test --tests "de.emaarco.TaskResourceTest.testEndpointToLoadTasks"
```

### Native Build
```bash
./gradlew build -Dquarkus.package.type=native
```

Container-based native build:
```bash
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

## Architecture

The project follows **Hexagonal Architecture** (Ports and Adapters):

### Package Structure
- `src/main/kotlin/de/emaarco/example/`
  - **`domain/`** - Core business entities (e.g., `Task`)
  - **`application/`** - Business logic layer
    - `port/inbound/` - Use case interfaces (what the application can do)
    - `port/outbound/` - External dependency interfaces (what the application needs)
    - `service/` - Use case implementations
  - **`adapter/`** - Infrastructure layer
    - `inbound/` - REST controllers (JAX-RS endpoints)
    - `outbound/` - Repository implementations

### Dependency Flow
Adapters → Ports → Application Services → Domain

Controllers depend on use case interfaces, services implement use cases and call outbound ports, repositories implement outbound ports.

### Key Technologies
- **Quarkus 3.28.1** - Application framework
- **Kotlin 2.2.20** - Primary language
- **Java 21** - JVM target
- **JAX-RS (RESTEasy)** - REST API endpoints
- **Jackson** - JSON serialization
- **CDI/Arc** - Dependency injection
- **JUnit 5 + RestAssured** - Testing

### Quarkus-Specific Notes
- `@ApplicationScoped` beans must be open - handled by `kotlin-allopen` plugin
- `@Path` and `@QuarkusTest` classes auto-configured for openness
- Dependency injection via constructor parameters (preferred) or `@Inject lateinit var`

## Testing

Tests are in `src/test/kotlin/de/emaarco/`
- `TaskResourceTest.kt` - Unit tests with `@QuarkusTest`
- `TaskResourceIT.kt` - Integration tests

RestAssured used for API testing with given/when/then syntax.

## Configuration

Version catalog: `gradle/libs.versions.toml`
- Centralized dependency management
- Use `libs.bundles.*` for grouped dependencies

Java/Kotlin toolchain configured for Java 21 in `build.gradle.kts`.