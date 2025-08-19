# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Quarkusaurus is a full-stack application demonstrating modern development practices. The backend is a Kotlin-based Quarkus application with PostgreSQL persistence using hexagonal architecture. The frontend is a React TypeScript application with Material-UI.

## Project Structure

```
├── service/          # Backend (Quarkus + Kotlin)
├── ui/              # Frontend (React + TypeScript)
├── stack/           # Docker infrastructure
├── CLAUDE.md        # This file
├── README.md        # Project documentation
└── LICENSE          # MIT License
```

## Development Commands

### Prerequisites
- Java 21 or later
- Node.js 18 or later
- Docker and Docker Compose

### Full-Stack Development

#### 1. Start Database
```bash
cd stack
docker compose up -d postgres
```

#### 2. Start Backend (in service/ folder)
```bash
cd service
./gradlew quarkusDev
```
- Backend runs on http://localhost:8081
- Dev UI available at http://localhost:8081/q/dev/
- API endpoints: http://localhost:8081/tasks

#### 3. Start Frontend (in ui/ folder)
```bash
cd ui
npm install  # First time only
npm run dev
```
- Frontend runs on http://localhost:5173
- Hot reload enabled for both backend and frontend

### Backend Commands (service/ folder)

#### Building
- **Standard build**: `./gradlew build`
- **Clean build**: `./gradlew clean build`
- **Native build**: `./gradlew build -Dquarkus.package.type=native`

#### Testing
- **Run all tests**: `./gradlew test` (uses H2 in-memory database)
- **Run integration tests**: `./gradlew quarkusIntTest`
- **Continuous testing mode**: `./gradlew quarkusTest`

#### Code Quality
- **Run all checks**: `./gradlew check`

### Frontend Commands (ui/ folder)

#### Development
- **Start dev server**: `npm run dev`
- **Build for production**: `npm run build`
- **Preview production build**: `npm run preview`

#### Code Quality
- **Lint code**: `npm run lint`
- **Fix linting issues**: `npm run lint:fix`
- **Format code**: `npm run format`

### Database Management
- **Start PostgreSQL**: `cd stack && docker compose up -d postgres`
- **Stop database**: `cd stack && docker compose down`
- **View database logs**: `cd stack && docker compose logs postgres`
- **Database connection**: `localhost:5433` (note the non-standard port)

## Architecture

### Backend Architecture (service/)
The backend follows hexagonal architecture (ports and adapters pattern):

- **Domain**: Core business logic in `service/src/main/kotlin/de/emaarco/example/domain/`
  - `Task.kt` - Main domain entity with JPA annotations
- **Application**: Use cases and ports in `service/src/main/kotlin/de/emaarco/example/application/`
  - `port/inbound/` - Input ports (use case interfaces)
  - `port/outbound/` - Output ports (repository interfaces)
  - `service/` - Use case implementations
- **Adapters**: External interfaces in `service/src/main/kotlin/de/emaarco/example/adapter/`
  - `inbound/` - REST controllers
  - `outbound/` - Repository implementations (PostgreSQL and in-memory)

### Frontend Architecture (ui/)
The frontend follows React best practices:

- **Components**: Reusable UI components in `ui/src/components/`
  - `TaskList.tsx` - Main task management interface
  - `TaskItem.tsx` - Individual task display component
  - `AddTaskForm.tsx` - Form for creating new tasks
- **Services**: API communication in `ui/src/services/`
  - `taskService.ts` - HTTP client for backend API
- **Types**: TypeScript definitions in `ui/src/types/`
  - `Task.ts` - Type definitions for task entities

## Technology Stack

### Backend
- **Framework**: Quarkus 3.25.0
- **Language**: Kotlin 2.2.0
- **Database**: PostgreSQL 16 with Hibernate ORM and Panache
- **Migrations**: Flyway
- **Build Tool**: Gradle with Kotlin DSL
- **Java Version**: 21
- **Testing**: JUnit 5 with REST Assured, H2 for tests
- **DI**: Jakarta CDI (provided by Quarkus Arc)
- **CORS**: Enabled for frontend integration

### Frontend
- **Framework**: React 19 with TypeScript
- **Build Tool**: Vite
- **UI Library**: Material-UI (MUI) v7
- **HTTP Client**: Axios
- **Code Quality**: ESLint + Prettier
- **Node Version**: 18+

## Database Configuration

### Development Database
- **Connection**: `jdbc:postgresql://localhost:5433/quarkusaurus`
- **Username/Password**: `quarkus/quarkus`
- **Docker service**: Defined in `stack/docker-compose.yml`

### Test Database
- Uses H2 in-memory database for fast test execution
- Configuration in `service/src/test/resources/application.properties`
- Automatically resets between tests

### Database Migrations
- Location: `service/src/main/resources/db/migration/`
- Naming convention: `V{version}__{description}.sql`
- Migrations run automatically on startup

## Key Configuration Files

### Backend (service/)
- **Main config**: `service/src/main/resources/application.properties`
- **Test config**: `service/src/test/resources/application.properties`
- **Gradle catalog**: `service/gradle/libs.versions.toml`
- **Build script**: `service/build.gradle.kts`

### Frontend (ui/)
- **Package config**: `ui/package.json`
- **TypeScript config**: `ui/tsconfig.json`
- **Vite config**: `ui/vite.config.ts`
- **ESLint config**: `ui/eslint.config.js`
- **Prettier config**: `ui/.prettierrc`

### Infrastructure (stack/)
- **Docker setup**: `stack/docker-compose.yml`

## Testing Strategy

### Unit Tests
- Repository tests: `PostgreSQLTaskRepositoryTest.kt`
- Service layer tests for business logic

### Integration Tests
- API tests: `TaskResourceTest.kt` and `TaskResourceIT.kt`
- Full stack testing with TestContainers
- Database transactions automatically rolled back between tests

### Test Database
- H2 in-memory database for fast test execution
- Hibernate configured to drop-and-create schema for tests
- Flyway migrations disabled in test profile
- @Transactional annotations required on test methods that modify data

## API Endpoints

- **GET /tasks** - Load all tasks
- **POST /tasks** - Create a new task
  - Body: `{"title": "string", "description": "string"}`

## Repository Implementations

- **PostgreSQLTaskRepository**: Production database implementation using Panache
- **InMemoryTaskRepository**: Simple in-memory implementation for testing/demo

## Docker Support

- **Application containers**: Multiple Dockerfiles in `src/main/docker/`
- **Database**: PostgreSQL service in `docker-compose.yml`
- **Development**: Run `docker compose up -d` to start dependencies