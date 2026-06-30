// @ts-check
const { test, expect } = require('@playwright/test');

test.describe('Quarkusaurus dino to-do', () => {
  test('renders the hatchery and the nest', async ({ page }) => {
    await page.goto('/');

    await expect(page).toHaveTitle(/Quarkusaurus/);
    await expect(page.getByRole('heading', { name: 'Quarkusaurus' })).toBeVisible();
    await expect(page.getByLabel('Title')).toBeVisible();
    await expect(page.getByRole('button', { name: /Hatch it/ })).toBeVisible();
  });

  test('hatches a new task and shows it in the nest', async ({ page }) => {
    await page.goto('/');

    // Unique title so the test is independent of any tasks already in memory.
    const title = `Guard the gate ${Date.now()}`;
    const description = 'Velociraptors are testing the fences again.';

    await page.getByLabel('Title').fill(title);
    await page.getByLabel('Description').fill(description);
    await page.getByRole('button', { name: /Hatch it/ }).click();

    const newTask = page.locator('li.task', { hasText: title });
    await expect(newTask).toBeVisible();
    await expect(newTask).toContainText(description);

    // The form resets after a successful hatch.
    await expect(page.getByLabel('Title')).toHaveValue('');
  });
});
