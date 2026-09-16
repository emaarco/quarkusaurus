# Files

- [Build and Run](build-and-run.md) - How the Gradle build is configured (version catalog, Quarkus and Kotlin plugins, allopen, Java 21 toolchain) and how to run Quarkusaurus in dev mode, as a packaged jar, as a native executable, or in a container.
- [CI and Repository Automation](ci-and-automation.md) - The GitHub Actions workflows that build and test every pull request, keep dependencies current through Dependabot with auto-merge, and generate and publish this OpenWiki.
- [Conductor Workspaces](conductor-workspaces.md) - How the checked-in Conductor configuration prepares each git-worktree workspace and starts Quarkus dev mode on a workspace-specific port so several workspaces can run in parallel.
