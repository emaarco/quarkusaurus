# OpenWiki Instructions

Write all pages in English.

## Scope

- Hexagonal architecture: domain, application (inbound/outbound ports, services), adapters (REST controllers, in-memory repository).
- The `/tasks` REST API: endpoints, request/response shapes, validation and error behaviour.
- The Dino To-Do frontend served from `src/main/resources/META-INF/resources/index.html` and how it talks to the API.
- Test strategy: RestAssured tests in `src/test/kotlin` and the Playwright e2e suite in `e2e/`.
- Build and run: Gradle tasks, Quarkus dev mode, native build, CI workflow.
- Conductor workspace setup in `.conductor/settings.toml`.

## Priorities

1. Architecture and dependency flow between packages.
2. API behaviour grounded in controllers, services and tests.
3. Developer workflow (build, test, run).

Keep pages concise. Prefer one diagram per page where it clarifies a flow.
