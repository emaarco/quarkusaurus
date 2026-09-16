---
type: overview
title: Quickstart
description: What Quarkusaurus is, the commands to build, run, and test it, and a task-oriented map pointing to the wiki page that answers each kind of question.
tags: [overview, quickstart, navigation, quarkus, kotlin]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-2a9daaac1604f238ef4c63fb
    resource: repo://build.gradle.kts
  - id: openwiki-source-9c5382d9900c65c9e9dc0ec4
    resource: repo://e2e/package.json
  - id: openwiki-source-23775c3de52f3ab95a13cb8b
    resource: repo://README.md
  - id: openwiki-source-59d0f64d6867f1938835f365
    resource: repo://src/main/kotlin/de/emaarco/example/adapter/inbound/CreateTaskController.kt
  - id: openwiki-source-bdda6309cc2a0c360a18ef14
    resource: repo://src/main/kotlin/de/emaarco/example/adapter/outbound/InMemoryTaskRepository.kt
  - id: openwiki-source-94dabaf0e1b66e77d76d432d
    resource: repo://src/main/kotlin/de/emaarco/example/application/service/CreateTaskService.kt
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# Quickstart

Quarkusaurus is a small Quarkus service written in Kotlin and built with Gradle. It exists to explore Quarkus and to demonstrate hexagonal architecture on a deliberately tiny problem: a task list with two operations, create and list. The same jar serves a JSON API at `/tasks` and a static "Dino To-Do" page at `/`.

## Run it in two minutes

```bash
./gradlew quarkusDev          # dev mode with live reload on http://localhost:8080
./gradlew build               # compile, run JVM tests, package build/quarkus-app/quarkus-run.jar
java -jar build/quarkus-app/quarkus-run.jar
./gradlew test                # RestAssured tests (in-process and against the packaged jar)
cd e2e && npm install && npx playwright install chromium && npm test   # browser tests
```

Java 21 is required; the Gradle toolchain provisions it if absent. `application.properties` is empty, so all Quarkus defaults apply.

## How the code is organised

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: an unescaped angle bracket inside a label breaks rendering; rephrase the label. -->
```text
flowchart LR
    UI[index.html] --> Ctl[adapter/inbound<br/>REST controllers]
    Ctl --> UC[application/port/inbound<br/>use cases]
    UC -.-> Svc[application/service]
    Svc --> Out[application/port/outbound]
    Out -.-> Repo[adapter/outbound<br/>InMemoryTaskRepository]
    Svc --> Dom[domain/Task]
```

Controllers depend on use-case interfaces, services implement them and call outbound ports, and a single in-memory repository implements the ports. Nothing is persisted beyond the process lifetime.

## Where to look

| I want to... | Read |
|--------------|------|
| Understand the layering and dependency rules | [Hexagonal Architecture](architecture/hexagonal-architecture.md) |
| Know how tasks get ids and where they are stored | [Task Domain and In-Memory Persistence](architecture/task-domain-and-persistence.md) |
| Call or change the `/tasks` endpoints | [Tasks REST API](api/tasks-rest-api.md) |
| Change the web page or its API calls | [Dino To-Do Frontend](frontend/dino-todo-ui.md) |
| Add or run tests | [Test Strategy](testing/test-strategy.md) |
| Configure Gradle, build native, or build a container | [Build and Run](operations/build-and-run.md) |
| Understand CI, Dependabot, or the wiki automation | [CI and Repository Automation](operations/ci-and-automation.md) |
| Run several workspaces in parallel with Conductor | [Conductor Workspaces](operations/conductor-workspaces.md) |

## Conventions to keep in mind

- **Open classes.** `@ApplicationScoped`, `@Path`, and `@QuarkusTest` classes are opened by the `kotlin-allopen` plugin; do not add `open` by hand except where subclassing needs it.
- **Dependency injection.** Services use constructor injection; controllers use `@Inject lateinit var`.
- **Dependencies.** Declare everything in `gradle/libs.versions.toml` and reference bundles from `build.gradle.kts`.
- **Tests package first.** `./gradlew test` always runs `quarkusBuild` beforehand because the integration test needs the jar.
- **Required CI check.** The pull request check is the job named `Build with Gradle`; keep that name.
