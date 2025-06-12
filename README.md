# 🌟 Quarkus Example

Welcome to this Quarkus playground! This repo is a light‑hearted attempt to master [Quarkus](https://quarkus.io/)—the supersonic, subatomic Java framework. Here we conjure up a tiny task service with a REST API so you can practice some Quarkus wizardry.

## 🧙‍♂️ About the Project
Think of this repository as a magic wand for your Quarkus journey. It shows how to:
- create a couple of REST endpoints
- wire the layers using a clean architecture style
- enjoy Quarkus' fast dev mode

## 📚 What is Quarkus?
Quarkus is a modern Java framework that starts lightning fast, uses very little memory, and makes developing microservices fun again.

## 🎬 Getting Started
1. **Start in Dev Mode**
   ```shell
   ./mvnw quarkus:dev
   ```
   Hot reload will be available at <http://localhost:8080/q/dev>.
2. **Package the App**
   ```shell
   ./mvnw package
   java -jar target/quarkus-app/quarkus-run.jar
   ```
   Need an über‑jar? Use `./mvnw package -Dquarkus.package.jar.type=uber-jar`.
3. **Build a Native Executable (optional)**
   ```shell
   ./mvnw package -Dnative
   ```
   Without GraalVM installed you can use `./mvnw package -Dnative -Dquarkus.native.container-build=true`.
4. **Run the Tests**
   ```shell
   ./mvnw test
   ```

## 🚀 REST Endpoints
- `GET /tasks` – fetch all tasks
- `POST /tasks` – create a task with `title` and `description`

## 🌌 License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

"Expecto *Quarkum*!" — May your services be as swift as a broomstick.
