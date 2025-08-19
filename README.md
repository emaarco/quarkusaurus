# 🦖 Quarkusaurus

Welcome to Quarkusaurus, a full-stack task management application demonstrating modern development practices with Quarkus and React. 

This repository showcases:
- **Backend**: Kotlin-based Quarkus application with PostgreSQL persistence using hexagonal architecture
- **Frontend**: React TypeScript application with Material-UI components
- **Infrastructure**: Docker Compose setup for development

The project explores what modern frameworks can offer – from fast startup times with Quarkus to reactive UIs with React – and see how loud this full-stack dino can roar! 🦕

## 🏗️ Project Structure

```
├── service/          # Backend (Quarkus + Kotlin)
├── ui/              # Frontend (React + TypeScript)
├── stack/           # Docker infrastructure
└── docs/            # Documentation
```

## 🚀 Quick Start

### Prerequisites
- Java 21 or later
- Node.js 18 or later  
- Docker and Docker Compose

### 1. Start the Database
```bash
cd stack
docker compose up -d postgres
```

### 2. Start the Backend
```bash
cd service
./gradlew quarkusDev
```
Backend runs at http://localhost:8081

### 3. Start the Frontend
```bash
cd ui
npm install
npm run dev
```
Frontend runs at http://localhost:5173

### 🎯 That's it! 
Visit http://localhost:5173 to see the task management interface in action! 

## 🌟 Features

### Backend Features
- **Hexagonal Architecture**: Clean separation of concerns with ports and adapters
- **PostgreSQL Integration**: Persistent data storage with Flyway migrations
- **Hot Reload**: Instant feedback during development with Quarkus Dev Mode
- **Type Safety**: Kotlin for robust, expressive code
- **Testing**: Comprehensive test suite with H2 in-memory database

### Frontend Features  
- **Modern React**: Built with React 19 and TypeScript
- **Material Design**: Beautiful UI components with Material-UI
- **Real-time Updates**: Responsive interface with loading states and error handling
- **Code Quality**: ESLint and Prettier for consistent code style
- **Fast Development**: Vite for lightning-fast builds and HMR

## 🧪 Development

### Backend Development (service/)
```bash
# Run with hot reload
cd service
./gradlew quarkusDev
```

```bash
# Run tests
cd service
./gradlew test
```

```bash
# Build application
cd service
./gradlew build
```

### Frontend Development (ui/)
```bash
# Start dev server
cd ui
npm run dev
```

```bash
# Lint and format code
cd ui
npm run lint
npm run format
```

```bash
# Build for production
cd ui
npm run build
```

## 🔗 Useful Links

### Backend Resources
- [Quarkus Official Website](https://quarkus.io/)
- [Quarkus Guides](https://quarkus.io/guides/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

### Frontend Resources  
- [React Documentation](https://react.dev/)
- [Material-UI Documentation](https://mui.com/)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)
- [Vite Documentation](https://vitejs.dev/)

## 🎨 License

This project is licensed under the MIT License.

--- 

*No dinosaurs were harmed during the development of this quarkus-example*