---
type: guide
title: Build and Run
description: How the Gradle build is configured (version catalog, Quarkus and Kotlin plugins, allopen, Java 21 toolchain) and how to run Quarkusaurus in dev mode, as a packaged jar, as a native executable, or in a container.
tags: [build, gradle, quarkus, native, docker, dev-mode]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-311b902b81b9fbe111c8359f
    resource: repo://.conductor/settings.toml
  - id: openwiki-source-715dace563ef484b6e8bd1e2
    resource: repo://.dockerignore
  - id: openwiki-source-675360e25390fa88714021b6
    resource: repo://.run/TestApplication.run.xml
  - id: openwiki-source-2a9daaac1604f238ef4c63fb
    resource: repo://build.gradle.kts
  - id: openwiki-source-81d5f1627e19148569f46f81
    resource: repo://gradle/libs.versions.toml
  - id: openwiki-source-e620d7484b72a53c7fa812cd
    resource: repo://settings.gradle.kts
  - id: openwiki-source-c9b543109873886695564dd9
    resource: repo://src/main/docker/Dockerfile.jvm
  - id: openwiki-source-e072f936167808135e27a7a4
    resource: repo://src/main/docker/Dockerfile.native
  - id: openwiki-source-ece093ad10b91f3da0b7e34f
    resource: repo://src/main/resources/application.properties
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# Build and Run

Quarkusaurus is a single-module Gradle project driven by the Gradle wrapper. Everything below runs from the repository root.

## Gradle configuration

| Concern | Where | What it does |
|---------|-------|--------------|
| Project name | `settings.gradle.kts` | Sets `rootProject.name = "quarkusaurus"` and adds the Gradle plugin portal plus Maven Central for plugin resolution. |
| Versions and bundles | `gradle/libs.versions.toml` | Single source of truth for Quarkus, Kotlin, Jackson Kotlin module, and RestAssured versions, grouped into bundles. |
| Plugins and dependencies | `build.gradle.kts` | Applies the Quarkus, Kotlin JVM, and Kotlin allopen plugins and wires the bundles. |
| JVM memory | `gradle.properties` | Gives the Gradle daemon a 2 GB heap and UTF-8 file encoding. |
| Wrapper | `gradle/wrapper/gradle-wrapper.properties` | Pins the Gradle distribution used by `./gradlew`. |

### Dependency bundles

The build never lists individual artifacts. It imports the Quarkus BOM as an `enforcedPlatform` and then adds four bundles from the catalog:

- `quarkus-core`: `quarkus-kotlin` and `quarkus-arc` (CDI).
- `quarkus-rest`: `quarkus-resteasy` and `quarkus-resteasy-jackson`, the classic RESTEasy stack with JSON.
- `kotlin-support`: the Kotlin standard library and `jackson-module-kotlin`, which lets Jackson construct Kotlin data classes.
- `quarkus-test` (test scope): `quarkus-junit5` and `rest-assured`.

Adding a dependency means adding it to the catalog and, usually, to a bundle. Dependabot updates the catalog, see [CI and Repository Automation](ci-and-automation.md).

### Kotlin and Quarkus specifics

- **allopen.** Kotlin classes are final, but Quarkus needs to proxy `@ApplicationScoped` beans and JAX-RS resources. The `allOpen` block opens classes annotated with `jakarta.ws.rs.Path`, `jakarta.enterprise.context.ApplicationScoped`, and `io.quarkus.test.junit.QuarkusTest`.
- **Toolchain.** Both the `java` and `kotlin` blocks target Java 21, so Gradle provisions or selects a JDK 21 regardless of the JDK running Gradle itself.
- **Logging in tests.** Every `Test` task sets `java.util.logging.manager` to JBoss LogManager, which Quarkus requires to avoid logging warnings.
- **Test ordering.** The `test` task depends on `quarkusBuild`. This guarantees the packaged application exists before tests run, which the `@QuarkusIntegrationTest` needs, at the cost of a full package on every test run.

## Running

### Dev mode

```bash
./gradlew quarkusDev
```

Starts the app on port 8080 with live reload and the Dev UI at `http://localhost:8080/q/dev/`. The Dino To-Do page is at `/` and the API at `/tasks`. The HTTP port can be overridden with `-Dquarkus.http.port=<port>`, which is how [Conductor](conductor-workspaces.md) runs several workspaces side by side.

`application.properties` is empty, so every Quarkus setting is at its default.

### Packaged jar

```bash
./gradlew build
java -jar build/quarkus-app/quarkus-run.jar
```

`build` compiles, runs the JVM test suite, and produces the fast-jar layout under `build/quarkus-app/`. The Playwright e2e suite boots this exact jar, see [Test Strategy](../testing/test-strategy.md).

### Native executable

```bash
./gradlew build -Dquarkus.package.type=native
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

The first form needs a local GraalVM or Mandrel; the second delegates compilation to a container so only a container runtime is required.

## Container images

`src/main/docker/` holds the four Quarkus-generated Dockerfiles:

| File | Base image | Copies from |
|------|------------|-------------|
| `Dockerfile.jvm` | `ubi8/openjdk-21` | `target/quarkus-app/` fast-jar layers |
| `Dockerfile.legacy-jar` | `ubi8/openjdk-21` | `target/*-runner.jar` and `target/lib/` |
| `Dockerfile.native` | `ubi8/ubi-minimal` | `target/*-runner` native binary |
| `Dockerfile.native-micro` | `quarkus-micro-image` | `target/*-runner` native binary |

These files and the accompanying `.dockerignore` still reference Maven's `target/` directory and `./mvnw package` in their comments, but this project builds with Gradle into `build/`. To build an image you must either copy `build/quarkus-app/` to `target/quarkus-app/` first or adjust the `COPY` paths. All images expose port 8080, run as a non-root user, and bind Quarkus to `0.0.0.0`.

## IDE run configuration

`.run/TestApplication.run.xml` is an IntelliJ Quarkus run configuration that invokes the Maven goal `quarkus:dev` against a `pom.xml`. Since the project has no `pom.xml`, this configuration is stale; use `./gradlew quarkusDev` or a Gradle run configuration instead.

## Related pages

- [CI and Repository Automation](ci-and-automation.md) runs `./gradlew build` and the e2e suite on every pull request.
- [Conductor Workspaces](conductor-workspaces.md) wraps dev mode with per-workspace ports.
- [Test Strategy](../testing/test-strategy.md)
- [Hexagonal Architecture](../architecture/hexagonal-architecture.md) for why allopen matters.
