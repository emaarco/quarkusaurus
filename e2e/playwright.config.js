// @ts-check
const { defineConfig, devices } = require('@playwright/test');

/**
 * Playwright config for the Quarkusaurus dino to-do UI.
 *
 * The `webServer` block boots the packaged Quarkus app (build it first with
 * `./gradlew build`) and waits until `/tasks` responds before the tests run.
 * Locally, an already-running server (e.g. `./gradlew quarkusDev`) is reused.
 */
module.exports = defineConfig({
  testDir: './tests',
  fullyParallel: false,
  workers: 1,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: 'http://localhost:8080',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
  },
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
  ],
  webServer: {
    command: 'java -jar ../build/quarkus-app/quarkus-run.jar',
    url: 'http://localhost:8080/tasks',
    reuseExistingServer: !process.env.CI,
    timeout: 120_000,
  },
});
