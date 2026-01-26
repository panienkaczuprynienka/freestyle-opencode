# Test Automation Framework

Stack: Java 21 + Maven + TestNG + Playwright (Chromium) + Allure (HTML report) + RestAssured.

## Local run

Install Playwright browsers (Chromium):

```bash
./mvnw -DskipTests exec:java -Dexec.classpathScope=test -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

Run smoke tests:

```bash
./mvnw test -Denv=stage -Dgroups=smoke -Dheadless=true -Dthreads=5
```

Note: the `smoke` group currently contains one intentionally failing test (`ui.tests.IntentionalFailureUiTest`) to validate screenshot/trace attachments in Allure.

Retry (TestNG):

- disabled by default
- enable via `-DretryCount=2` (on CI it is enabled)

There is a demo test that fails once and passes on retry: `ui.tests.FlakyOnceUiTest`.

Generate Allure HTML report:

```bash
rm -rf target/allure-report
npx -y allure@3.0.1 awesome target/allure-results --output target/allure-report --name "Automation Report"
open target/allure-report/index.html
```

## Configuration

Defaults come from `src/test/resources/config/${env}.properties`.

Common overrides:

- `-Denv=stage`
- `-DbaseUrl=https://playwright.dev`
- `-Dheadless=true`
- `-Dtrace=on-failure|off|on`
- `-Dvideo=on-failure|off`

## CI

GitHub Actions runs `smoke` group on Chromium, generates an Allure Report 3 (Awesome) HTML report and:

- uploads it as an artifact
- publishes it to GitHub Pages (branch `gh-pages`) under:
  - `/latest/`
  - `/reports/<run_number>/`

Each CI run adds direct links to the report in the GitHub Actions job summary.

Note: in the repository settings, configure GitHub Pages to serve from the `gh-pages` branch (root).
