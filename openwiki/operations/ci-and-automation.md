---
type: operations
title: CI and Repository Automation
description: The GitHub Actions workflows that build and test every pull request, keep dependencies current through Dependabot with auto-merge, and generate and publish this OpenWiki.
tags: [ci, github-actions, dependabot, automerge, openwiki, github-pages]
verified:
  - by: openwiki/0.5.2
    at: 2026-09-16T13:49:10.171Z
sources:
  - id: openwiki-source-1307a98427393d045f958ba3
    resource: repo://.github/CODEOWNERS
  - id: openwiki-source-79b37831c9c81206da1d88ec
    resource: repo://.github/dependabot.yml
  - id: openwiki-source-164e2da859b5277df81c7d94
    resource: repo://.github/workflows/ci.yml
  - id: openwiki-source-7818024f05c1336008c853c1
    resource: repo://.github/workflows/dependabot-automerge.yml
  - id: openwiki-source-33403bcef8aaabc7af1eb005
    resource: repo://.github/workflows/openwiki-pages.yml
  - id: openwiki-source-6d4b4e707b8d60b6ccfa3425
    resource: repo://.github/workflows/openwiki-update.yml
  - id: openwiki-source-f5a489e5822d87c0b8fc66ef
    resource: repo://.mcp.json
generated: { by: "claude-code", at: "2026-09-16T13:49:10.171Z" }
---

# CI and Repository Automation

All automation lives in `.github/`. Four workflows and a Dependabot configuration cover three concerns: verifying changes, updating dependencies, and maintaining this wiki. Every third-party action is pinned to a full commit SHA with a version comment, and Dependabot keeps those pins current.

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: an unescaped angle bracket inside a label breaks rendering; rephrase the label. -->
```text
flowchart LR
    PR[Pull request] --> CI[ci.yml<br/>Build with Gradle]
    Dep[Dependabot PR] --> CI
    Dep --> AM[dependabot-automerge.yml]
    Cron[Monday 06:00 UTC] --> WU[openwiki-update.yml]
    WU --> WPR[PR openwiki/update]
    WPR -- merged --> Main[main]
    Main -- openwiki/** changed --> Pages[openwiki-pages.yml<br/>GitHub Pages]
```

## Pull request verification: `ci.yml`

Runs on every pull request and on pushes to `main`. A concurrency group cancels superseded runs on the same ref. The single job is deliberately named **Build with Gradle** because that name is the required status check on `main`; renaming it would silently stop protecting the branch.

The job runs, in order:

1. Checkout and JDK 21 (Temurin) with Gradle caching.
2. `./gradlew build --no-daemon`, which compiles, runs the RestAssured `@QuarkusTest` and `@QuarkusIntegrationTest` suites, and produces `build/quarkus-app/quarkus-run.jar`.
3. Node.js 22 with npm caching keyed on `e2e/package-lock.json`, then `npm ci` and Chromium installation for Playwright.
4. `npm test` in `e2e/`, which boots the packaged jar and runs the browser tests.
5. Upload of `e2e/playwright-report/` as an artifact, retained for seven days, even when tests fail.

Because step 4 depends on the jar from step 2, the two suites run sequentially in one job. See [Test Strategy](../testing/test-strategy.md).

## Dependency updates

`dependabot.yml` defines three monthly update streams, each grouped so one pull request carries all updates of that ecosystem:

| Ecosystem | Directory | Group |
|-----------|-----------|-------|
| Gradle (version catalog) | `/` | `backend-dependencies` |
| npm (Playwright suite) | `/e2e` | `e2e-dependencies` |
| GitHub Actions | `/` | `github-actions` |

`dependabot-automerge.yml` runs on Dependabot pull requests only. It reads the update type via `dependabot/fetch-metadata` and, for semver patch and minor updates, enables squash auto-merge with a five-attempt retry loop to absorb a known race when enabling auto-merge. Major updates are left for a human. Auto-merge still waits for the required **Build with Gradle** check, so a failing build blocks the merge.

`CODEOWNERS` assigns every file to `@emaarco`, so all pull requests request that review automatically.

## OpenWiki generation: `openwiki-update.yml`

Runs every Monday at 06:00 UTC or on manual dispatch, with a non-cancelling concurrency group so two updates never overlap. Steps:

1. Mint a GitHub App token from the `RELEASE_PLEASE_APP_*` variables and secret, used later for the pull request so that CI runs on it.
2. Check out with full history so OpenWiki can diff against the last documented commit.
3. Install OpenWiki 0.5.2 globally with Mermaid and jsdom, and install the Claude Code integration into the project.
4. Run Claude Code via `anthropics/claude-code-action` with the subscription OAuth token. The prompt asks for an update run; `.mcp.json` registers the `openwiki mcp --host claude` server and the allowed tools are restricted to the OpenWiki MCP tools, file reads and writes, and a few read-only git commands. The step is `continue-on-error` so partial progress is still captured.
5. Delete the transient `openwiki/.run.json`.
6. Open or update a pull request on branch `openwiki/update` from `openwiki/`, `AGENTS.md`, and `CLAUDE.md`, with signed commits.
7. Enable squash auto-merge when the OpenWiki step succeeded, disable it when it failed, then fail the workflow to surface the error.

The result is that a successful weekly update merges itself after CI passes, while a failed one leaves a reviewable pull request.

## Publishing: `openwiki-pages.yml`

Triggered by pushes to `main` that touch `openwiki/**`, or manually. It installs OpenWiki, runs `openwiki visualize openwiki --export site`, and deploys the `site` directory to GitHub Pages through the standard configure, upload, and deploy actions under the `github-pages` environment.

## Local counterpart

Developers run the same lifecycle interactively through the openwiki skill and the MCP server declared in `.mcp.json`. The `.openwikiignore` file excludes build outputs, Playwright artifacts, and node modules from OpenWiki's source scan.

## Related pages

- [Test Strategy](../testing/test-strategy.md)
- [Build and Run](build-and-run.md)
