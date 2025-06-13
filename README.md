# 🦖 Quarkusaurus‚

Welcome to Quarkusaurus, my personal playground for exploring Quarkus –
a modern Java framework tailored for cloud-native applications.
This repository contains a quarkus example service built with Kotlin & Gradle.
Its goal? To explore what Quarkus can offer –
from fast startup times to cool developer tools –
and see how loud this framework can roar. 🦕

## 🌟 What's Quarkus?

Quarkus is a Java framework designed for fast startup times,
minimal memory usage, and cloud-native environments.
It’s especially great for microservices and container deployments –
like a multi-talented dino that adapts to any
environment and still has time to learn some tricks. 🦖

## 🎮 Getting Started

### Development Mode

Run the application in development mode with hot reload:

```shell script
./gradlew quarkusDev
```

The Dev UI is available at http://localhost:8080/q/dev/. It's perfect for experimenting and seeing changes instantly –
like testing new tools in your dino lab without ever leaving the jungle. 🧪🌴

### Packaging

Package your application:

```shell script
./gradlew build
```

This creates a `quarkus-run.jar` in `build/quarkus-app/`. Run it with:

```shell script
java -jar build/quarkus-app/quarkus-run.jar
```

With everything bundled and ready to go, you’re just one command away
from setting your Quarkus creature loose in the wild. 🦕

### Native Build

Create a native executable:

```shell script
./gradlew build -Dquarkus.package.type=native
```

Or use container build:

```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

For maximum performance, this build runs fast and light –
like a well-evolved dino built for speed and efficiency. 🏃‍♂️🦖

## 🔗 Useful Links

- [Quarkus Official Website](https://quarkus.io/)
- [RESTEasy Guide](https://quarkus.io/guides/resteasy)

## 🎨 License

This project is licensed under the MIT License.

--- 

*No dinosaurs were harmed during the development of this quarkus-example*