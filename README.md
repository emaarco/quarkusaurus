# 🚀 Quarkus-Example

Welcome to my Quarkus playground! This repository is where I'm exploring Quarkus - a modern Java framework designed for
cloud-native applications. It's known for its fast startup times and efficient resource usage - like a cheetah on Red
Bull! 🐆

## 🌟 What's Quarkus?

Quarkus is a Java framework that brings together the best of Java with modern cloud-native features. It's particularly
good at creating lightweight applications that start up quickly and use resources efficiently. Perfect for microservices
and cloud deployments.

## 🎮 Getting Started

ç

### Development Mode

Run the application in development mode with hot reload:

```shell script
./mvnw quarkus:dev
```

The Dev UI is available at http://localhost:8080/q/dev/

### Packaging

Package your application:

```shell script
./mvnw package
```

This creates a `quarkus-run.jar` in `target/quarkus-app/`. Run it with:

```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

For an über-jar:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

### Native Build

Create a native executable:

```shell script
./mvnw package -Dnative
```

Or use container build:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## 🔗 Useful Links

- [Quarkus Official Website](https://quarkus.io/)
- [RESTEasy Guide](https://quarkus.io/guides/resteasy)

## 🎨 License

This project is licensed under the MIT License.

