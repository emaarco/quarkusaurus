---
type: concept
title: Task Domain and In-Memory Persistence
description: The Task entity, how ids are generated, the outbound ports that abstract storage, and the lifecycle and test implications of the in-memory repository that backs them.
tags: [domain, persistence, in-memory, task, ports]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-bdda6309cc2a0c360a18ef14
    resource: repo://src/main/kotlin/de/emaarco/example/adapter/outbound/InMemoryTaskRepository.kt
  - id: openwiki-source-0405451a6dde0bffc7420ffc
    resource: repo://src/main/kotlin/de/emaarco/example/application/port/outbound/LoadTasksPort.kt
  - id: openwiki-source-eab27765be9cad5c22ce4d86
    resource: repo://src/main/kotlin/de/emaarco/example/application/port/outbound/SaveTaskPort.kt
  - id: openwiki-source-94dabaf0e1b66e77d76d432d
    resource: repo://src/main/kotlin/de/emaarco/example/application/service/CreateTaskService.kt
  - id: openwiki-source-8c6b2e86931c2431c0ffd968
    resource: repo://src/main/kotlin/de/emaarco/example/domain/Task.kt
  - id: openwiki-source-dee6aa68ea32c80a02769a80
    resource: repo://src/test/kotlin/de/emaarco/TaskResourceTest.kt
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# Task Domain and In-Memory Persistence

The whole domain model of Quarkusaurus is one entity, and the whole persistence layer is one list. This page explains both and, more importantly, the consequences of that choice for tests and runtime behaviour.

## The Task entity

`Task` (`src/main/kotlin/de/emaarco/example/domain/Task.kt`) is an immutable Kotlin data class with three string fields: `id`, `title`, and `description`. It has no framework annotations and no dependencies beyond `java.util.UUID`, so it can be used freely from every layer.

The primary constructor takes all three fields. A secondary constructor takes only `title` and `description` and assigns a random UUID string as the id. `CreateTaskService` always uses the secondary constructor, so ids are generated inside the domain at creation time rather than by the store. The domain enforces no invariants: empty titles and descriptions are accepted.

Because `Task` is a data class, Jackson serializes it with exactly those three properties, which is why the JSON returned by `GET /tasks` has the shape `{ "id", "title", "description" }`.

## Outbound ports

Two interfaces in `application/port/outbound/` isolate the application from storage:

- `SaveTaskPort.saveTask(task: Task)` persists a task and returns nothing.
- `LoadTasksPort.loadTasks(): List<Task>` returns all tasks.

There is no port for finding by id, updating, or deleting. The API surface is intentionally limited to append and list.

## InMemoryTaskRepository

`InMemoryTaskRepository` in `adapter/outbound/` implements both ports over a private `mutableListOf<Task>()`. `saveTask` appends; `loadTasks` returns the underlying mutable list itself, not a copy.

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: a semicolon inside a label breaks rendering; rephrase the label. -->
```text
flowchart LR
    Svc[CreateTaskService] -- saveTask --> Repo[(InMemoryTaskRepository<br/>mutableListOf&lt;Task&gt;)]
    Load[LoadTasksService] -- loadTasks --> Repo
```

### Lifecycle and consequences

- **Scope and lifetime.** The bean is `@ApplicationScoped`, so a single list lives for the lifetime of the Quarkus application. Data survives across requests but is lost on restart, and a dev-mode hot reload that recreates the application discards it.
- **Ordering.** Tasks are returned in insertion order, which the frontend relies on to show the nest in creation sequence.
- **Concurrency.** The list is an unsynchronized `ArrayList`. Concurrent `POST` requests can race, and a `GET` iterating the list while a `POST` appends may throw a `ConcurrentModificationException` during JSON serialization. For a demo this is acceptable; a production adapter would need a thread-safe structure or a real database.
- **Aliasing.** Because `loadTasks` hands out the live list, any caller that mutated the result would mutate the store. Current callers only read it.

### Consequences for tests

The `@QuarkusTest` suite asserts that `GET /tasks` returns `[]`. That holds because each test JVM boots a fresh application and no test creates a task first. The Playwright end-to-end test, which does create tasks, uses a timestamped title so it stays independent of tasks already present in a reused dev server. See [Test Strategy](../testing/test-strategy.md).

## Replacing the store

A database-backed adapter only needs to implement the two outbound ports as an `@ApplicationScoped` bean and remove (or mark as `@Alternative`) the in-memory one. Services, controllers, and the domain remain unchanged, which is the payoff of the layering described in [Hexagonal Architecture](hexagonal-architecture.md).

## Related pages

- [Hexagonal Architecture](hexagonal-architecture.md)
- [Tasks REST API](../api/tasks-rest-api.md)
- [Test Strategy](../testing/test-strategy.md)
