---
type: testing
title: Test Strategy
description: The two test layers of Quarkusaurus, RestAssured tests in JVM and packaged mode plus a Playwright browser suite, how each boots the application, what they cover, and how to run them locally and in CI.
tags: [testing, quarkus-test, rest-assured, playwright, e2e, integration-test]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-2a9daaac1604f238ef4c63fb
    resource: repo://build.gradle.kts
  - id: openwiki-source-640277475c14989bca2150b6
    resource: repo://e2e/playwright.config.js
  - id: openwiki-source-5849d38d832288c50c5c9d2b
    resource: repo://e2e/tests/app.spec.js
  - id: openwiki-source-019c9a14ada8dd3f78ea2129
    resource: repo://src/test/kotlin/de/emaarco/TaskResourceIT.kt
  - id: openwiki-source-dee6aa68ea32c80a02769a80
    resource: repo://src/test/kotlin/de/emaarco/TaskResourceTest.kt
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# Test Strategy

Quarkusaurus verifies the application at two levels: HTTP-level tests written in Kotlin with RestAssured, and browser-level end-to-end tests written in JavaScript with Playwright. Both are small, but together they exercise every layer from the static frontend through the controllers to the in-memory store.

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: an unescaped angle bracket inside a label breaks rendering; rephrase the label. -->
```text
flowchart TB
    subgraph JVM["./gradlew test"]
        QT[TaskResourceTest<br/>@QuarkusTest: in-process app]
        IT[TaskResourceIT<br/>@QuarkusIntegrationTest: packaged jar]
    end
    subgraph E2E["npm test in e2e/"]
        PW[app.spec.js<br/>Playwright + Chromium]
        WS[webServer: java -jar quarkus-run.jar]
        PW --> WS
    end
    QT --> API[/tasks API/]
    IT --> API
    WS --> UI[index.html + /tasks]
```

## JVM tests with RestAssured

The Kotlin tests live in `src/test/kotlin/de/emaarco/`.

**`TaskResourceTest`** is annotated `@QuarkusTest`, so Quarkus starts the application once in the test JVM and RestAssured targets it automatically. Its single test calls `GET /tasks` and asserts a `200` status with the exact body `[]`. That assertion pins two facts: the endpoint is wired, and a freshly started application has an empty store. Any future test that creates a task in the same class would break this assertion unless ordering or cleanup is added, because the in-memory repository is shared for the application lifetime.

**`TaskResourceIT`** extends `TaskResourceTest` and is annotated `@QuarkusIntegrationTest`. It inherits the same test method but runs it against the packaged artifact rather than an in-process application. That is why the Gradle `test` task declares `dependsOn("quarkusBuild")`: the jar must exist before the integration test can launch it. In effect the same contract is checked twice, once in dev-style mode and once against what will actually ship.

Run them with:

```bash
./gradlew test
./gradlew test --tests "de.emaarco.TaskResourceTest.testEndpointToLoadTasks"
```

The `kotlin-allopen` plugin opens `@QuarkusTest` classes, and `TaskResourceTest` is additionally declared `open` so it can be subclassed.

## Browser tests with Playwright

The `e2e/` directory is a self-contained npm project that pins `@playwright/test` as its only dependency and exposes three scripts: `test`, `test:headed`, and `report`.

### How the app is started

`playwright.config.js` defines a `webServer` that runs `java -jar ../build/quarkus-app/quarkus-run.jar` and polls `http://localhost:8080/tasks` until it responds, with a two-minute timeout. Consequences:

- You must run `./gradlew build` first so the jar exists.
- Locally, `reuseExistingServer` is true, so an already-running server on 8080 (for example `./gradlew quarkusDev`) is used instead. In CI it is false, so the packaged jar is always what gets tested.
- `baseURL` is `http://localhost:8080`; a server on another port is not detected.

The suite runs a single worker without parallelism against Chromium only, retries once in CI, and records a trace on first retry and screenshots on failure. Reports are written to `e2e/playwright-report/`.

### What the tests cover

`tests/app.spec.js` contains two tests:

1. **Renders the hatchery and the nest.** Opens `/` and checks the page title, the `Quarkusaurus` heading, the `Title` field, and the hatch button.
2. **Hatches a new task.** Fills title and description, submits, and expects a `li.task` containing both to appear, and the title input to be empty again. The title includes `Date.now()` so the test passes even when a reused dev server already holds tasks.

Together these cover the page load `GET`, the form `POST`, the follow-up reload, and the form reset described in [Dino To-Do Frontend](../frontend/dino-todo-ui.md).

Run them with:

```bash
./gradlew build
cd e2e && npm install && npx playwright install chromium
npm test
npm run report
```

## In CI

The `Build with Gradle` job in `ci.yml` runs `./gradlew build` (which includes both JVM test classes) and then the Playwright suite in the same job, uploading the HTML report as an artifact. See [CI and Repository Automation](../operations/ci-and-automation.md).

## Gaps worth knowing

- No test exercises `POST /tasks` at the HTTP level; only the browser test creates a task.
- There are no unit tests for services or the repository; the ports are simple enough that the HTTP tests cover them.
- Error paths (malformed JSON, missing fields) are untested.

## Related pages

- [Tasks REST API](../api/tasks-rest-api.md)
- [Dino To-Do Frontend](../frontend/dino-todo-ui.md)
- [Task Domain and In-Memory Persistence](../architecture/task-domain-and-persistence.md)
- [CI and Repository Automation](../operations/ci-and-automation.md)
