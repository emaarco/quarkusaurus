---
type: operations
title: Conductor Workspaces
description: How the checked-in Conductor configuration prepares each git-worktree workspace and starts Quarkus dev mode on a workspace-specific port so several workspaces can run in parallel.
tags: [conductor, worktree, dev-mode, ports, developer-workflow]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-311b902b81b9fbe111c8359f
    resource: repo://.conductor/settings.toml
  - id: openwiki-source-640277475c14989bca2150b6
    resource: repo://e2e/playwright.config.js
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# Conductor Workspaces

[Conductor](https://conductor.build) creates one git worktree per task and runs each in its own workspace. Quarkusaurus ships a shared configuration in `.conductor/settings.toml` so every workspace is ready to run without manual steps. The file declares a schema URL and a single `[scripts]` table with three keys.

## Setup script

```toml
setup = "cd e2e && npm install && npx playwright install chromium"
```

Runs once when a workspace is created. It installs the Playwright test package into `e2e/node_modules` and downloads the Chromium browser binary. Nothing is done for the Gradle side because the Gradle wrapper resolves the JDK toolchain and dependencies on first use.

## Run script

```toml
run = "./gradlew quarkusDev -Dquarkus.http.port=$CONDUCTOR_PORT -Ddebug=$((CONDUCTOR_PORT + 1))"
run_mode = "concurrent"
```

The run button starts Quarkus dev mode with two workspace-specific ports:

- `-Dquarkus.http.port=$CONDUCTOR_PORT` moves the HTTP listener off the default 8080 to the port Conductor assigned to this workspace.
- `-Ddebug=$((CONDUCTOR_PORT + 1))` places the JVM debug listener on the next port so debuggers from different workspaces do not collide.

`run_mode = "concurrent"` tells Conductor that multiple workspaces may run this script at the same time.

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: an unescaped angle bracket inside a label breaks rendering; rephrase the label. -->
```text
flowchart LR
    W1[Workspace A<br/>CONDUCTOR_PORT=3001] --> Q1[quarkusDev :3001<br/>debug :3002]
    W2[Workspace B<br/>CONDUCTOR_PORT=3005] --> Q2[quarkusDev :3005<br/>debug :3006]
```

## Interaction with the rest of the tooling

- **Frontend.** The Dino To-Do page uses relative URLs, so it works unchanged on any port. See [Dino To-Do Frontend](../frontend/dino-todo-ui.md).
- **JVM tests.** `./gradlew test` starts its own Quarkus instance on the Quarkus test port and is unaffected by `CONDUCTOR_PORT`.
- **Playwright.** The e2e config hard-codes `http://localhost:8080` for both `baseURL` and the `webServer` health check. It therefore does not reuse a Conductor-started dev server on another port. Run `npm test` after `./gradlew build` and Playwright will boot the packaged jar on 8080 itself. See [Test Strategy](../testing/test-strategy.md).
- **Configuration.** Because `application.properties` is empty, the system property override is the only port configuration in the project. See [Build and Run](build-and-run.md).

## Related pages

- [Build and Run](build-and-run.md)
- [Test Strategy](../testing/test-strategy.md)
- [Dino To-Do Frontend](../frontend/dino-todo-ui.md)
