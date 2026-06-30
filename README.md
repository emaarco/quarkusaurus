# 🦖 Quarkusaurus

[![CI](https://github.com/emaarco/quarkusaurus/actions/workflows/ci.yml/badge.svg)](https://github.com/emaarco/quarkusaurus/actions/workflows/ci.yml)

Welcome to Quarkusaurus, my personal playground for exploring Quarkus –
a modern Java framework tailored for cloud-native applications.
This repository contains a quarkus example service built with Kotlin & Gradle.
Its goal? To explore what Quarkus can offer –
from fast startup times to cool developer tools –
and see how loud this framework can roar. 🦕

## 🦕 The Dino To-Do App

Quarkus serves its own little frontend: a prehistoric task manager where you
*hatch* new task-eggs into the nest. The page lives at
[`http://localhost:8080/`](http://localhost:8080/) and talks to the same
`/tasks` REST API — no separate frontend server needed.

The static page is served straight from
`src/main/resources/META-INF/resources/index.html` (Quarkus auto-serves files
in `META-INF/resources/`), so it ships inside the very same jar as the API.

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

## 🧪 End-to-End Tests (Playwright)

Browser-level tests drive the dino UI exactly like a user would — loading the
page and hatching a task through the form — and live in [`e2e/`](e2e/).

```shell script
# 1. Build the app so the packaged jar exists
./gradlew build

# 2. Install the Playwright toolchain (first run only)
cd e2e
npm install
npx playwright install chromium

# 3. Run the suite
npm test
```

Playwright boots the packaged app (`build/quarkus-app/quarkus-run.jar`) on
port 8080 automatically and waits for it to be ready before testing. If a
server is already running locally (e.g. `./gradlew quarkusDev`), it is reused.
The HTML report can be opened afterwards with `npm run report`.

These complement the existing JVM tests (`./gradlew test`), which cover the
REST layer with RestAssured. 🦖

Both suites run automatically on every pull request via
[GitHub Actions](.github/workflows/ci.yml).

## 🎛️ Conductor

This repo ships a shared [Conductor](https://conductor.build) config
([`.conductor/settings.toml`](.conductor/settings.toml)) so parallel workspaces
just work:

- **Setup** installs the Playwright e2e toolchain into the new workspace.
- **Run** starts `quarkusDev` on the workspace's assigned `$CONDUCTOR_PORT`
  (debug on `+1`), so multiple workspaces run side by side without port clashes.

## 🔗 Useful Links

- [Quarkus Official Website](https://quarkus.io/)
- [RESTEasy Guide](https://quarkus.io/guides/resteasy)

## 🎨 License

This project is licensed under the MIT License.

--- 

*No dinosaurs were harmed during the development of this quarkus-example*