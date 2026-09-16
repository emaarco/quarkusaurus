---
type: architecture
title: Hexagonal Architecture
description: How Quarkusaurus applies ports and adapters across its domain, application, and adapter packages, how CDI wires the layers together, and which direction dependencies are allowed to point.
tags: [architecture, hexagonal, ports-and-adapters, cdi, kotlin]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-2a9daaac1604f238ef4c63fb
    resource: repo://build.gradle.kts
  - id: openwiki-source-59d0f64d6867f1938835f365
    resource: repo://src/main/kotlin/de/emaarco/example/adapter/inbound/CreateTaskController.kt
  - id: openwiki-source-968ca62c460ab1ee0a7f9927
    resource: repo://src/main/kotlin/de/emaarco/example/adapter/inbound/LoadTasksController.kt
  - id: openwiki-source-bdda6309cc2a0c360a18ef14
    resource: repo://src/main/kotlin/de/emaarco/example/adapter/outbound/InMemoryTaskRepository.kt
  - id: openwiki-source-c39ebaeff6ad6aabafacc5dc
    resource: repo://src/main/kotlin/de/emaarco/example/application/port/inbound/CreateTaskUseCase.kt
  - id: openwiki-source-0405451a6dde0bffc7420ffc
    resource: repo://src/main/kotlin/de/emaarco/example/application/port/outbound/LoadTasksPort.kt
  - id: openwiki-source-eab27765be9cad5c22ce4d86
    resource: repo://src/main/kotlin/de/emaarco/example/application/port/outbound/SaveTaskPort.kt
  - id: openwiki-source-94dabaf0e1b66e77d76d432d
    resource: repo://src/main/kotlin/de/emaarco/example/application/service/CreateTaskService.kt
  - id: openwiki-source-a02fac51d91a3aa00e083b50
    resource: repo://src/main/kotlin/de/emaarco/example/application/service/LoadTasksService.kt
  - id: openwiki-source-8c6b2e86931c2431c0ffd968
    resource: repo://src/main/kotlin/de/emaarco/example/domain/Task.kt
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# Hexagonal Architecture

Quarkusaurus is a deliberately small demonstration of ports and adapters. All production code lives under `src/main/kotlin/de/emaarco/example/` and is divided into three rings. The rule that holds the design together is that dependencies only point inward: adapters know about ports, services implement ports and use the domain, and the domain knows nothing about the rest.

## Layers and packages

| Ring | Package | Contents | May depend on |
|------|---------|----------|---------------|
| Domain | `domain/` | `Task` entity | nothing |
| Application | `application/port/inbound/` | Use-case interfaces (`CreateTaskUseCase`, `LoadTasksUseCase`) | domain |
| Application | `application/port/outbound/` | Required-dependency interfaces (`SaveTaskPort`, `LoadTasksPort`) | domain |
| Application | `application/service/` | Use-case implementations (`CreateTaskService`, `LoadTasksService`) | ports, domain |
| Adapter | `adapter/inbound/` | JAX-RS controllers | inbound ports |
| Adapter | `adapter/outbound/` | `InMemoryTaskRepository` | outbound ports, domain |

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: an unescaped angle bracket inside a label breaks rendering; rephrase the label. -->
```text
flowchart LR
    subgraph Adapters
        Ctl[REST controllers]
        Repo[InMemoryTaskRepository]
    end
    subgraph Application
        In[Inbound ports<br/>CreateTaskUseCase, LoadTasksUseCase]
        Svc[Services]
        Out[Outbound ports<br/>SaveTaskPort, LoadTasksPort]
    end
    Dom[Domain: Task]
    Ctl --> In
    Svc -. implements .-> In
    Svc --> Out
    Repo -. implements .-> Out
    Svc --> Dom
    Repo --> Dom
```

## Ports

Inbound ports describe what the application can do. Each is a single-method Kotlin interface, and `CreateTaskUseCase` nests its own `CreateTaskCommand` data class so callers never construct a domain `Task` directly. The command carries only `title` and `description`; id generation is a service and domain concern.

Outbound ports describe what the application needs from the outside world. `SaveTaskPort` and `LoadTasksPort` are separate interfaces even though one class implements both, which lets each service declare the narrowest dependency it actually uses: `CreateTaskService` only asks for `SaveTaskPort`, `LoadTasksService` only for `LoadTasksPort`.

## Services

Services are thin. `CreateTaskService` logs, builds a `Task` from the command, and hands it to the save port. `LoadTasksService` logs and forwards to the load port. There is no transaction handling, validation, or mapping, so the services currently exist to enforce the boundary rather than to host complex logic.

## Adapters

The inbound adapters are two JAX-RS resource classes that both mount at `/tasks`; see [Tasks REST API](../api/tasks-rest-api.md). They depend only on inbound port interfaces, never on service classes.

The single outbound adapter, `InMemoryTaskRepository`, implements both outbound ports over a mutable list; see [Task Domain and In-Memory Persistence](task-domain-and-persistence.md).

## CDI wiring

Quarkus Arc resolves every binding by type without any producer or qualifier:

- Services are `@ApplicationScoped` and receive their outbound port through constructor injection.
- `InMemoryTaskRepository` is `@ApplicationScoped` and is the only bean implementing `SaveTaskPort` and `LoadTasksPort`, so both injections resolve unambiguously.
- Controllers use `@Inject lateinit var` field injection for their use case. Each use-case interface has exactly one implementation.

Kotlin classes are final by default, which would break Arc's client proxies for `@ApplicationScoped` beans. The build applies the `kotlin-allopen` plugin to classes annotated with `jakarta.ws.rs.Path`, `jakarta.enterprise.context.ApplicationScoped`, and `io.quarkus.test.junit.QuarkusTest`, so no class in the code base needs an explicit `open` modifier. The base test class `TaskResourceTest` is still declared `open` explicitly because it is subclassed by the integration test.

## Extension seams

Because the boundaries are interfaces, the natural extension points are:

- **New use case:** add an inbound port interface, a service that implements it, and a controller that injects it.
- **Real persistence:** implement `SaveTaskPort` and `LoadTasksPort` with a database-backed bean and remove or `@Alternative` the in-memory one. Services and controllers remain untouched.
- **Alternative delivery mechanism:** any adapter (messaging, CLI, scheduler) can drive the same inbound ports.

## Related pages

- [Tasks REST API](../api/tasks-rest-api.md)
- [Task Domain and In-Memory Persistence](task-domain-and-persistence.md)
- [Build and Run](../operations/build-and-run.md) for the Gradle plugin configuration that enables allopen.
