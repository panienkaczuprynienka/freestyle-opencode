#!/usr/bin/env bash

set -euo pipefail

: "${RUN_NUMBER:?RUN_NUMBER is required}"
: "${REPO_OWNER:?REPO_OWNER is required}"
: "${REPO_NAME:?REPO_NAME is required}"

REPORT_BASE_URL="https://${REPO_OWNER}.github.io/${REPO_NAME}"
CURRENT_URL="${REPORT_BASE_URL}/reports/${RUN_NUMBER}/"
LATEST_URL="${REPORT_BASE_URL}/latest/"

rm -rf site
mkdir -p "site/reports/${RUN_NUMBER}"
mkdir -p site/latest

cp -R target/allure-report/. "site/reports/${RUN_NUMBER}/"
cp -R target/allure-report/. site/latest/

if [ -f gh-pages/history.json ]; then
  cp gh-pages/history.json site/history.json
fi

cat > site/index.html <<EOF
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Allure Reports</title>
</head>
<body>
  <h1>Allure Reports</h1>
  <p><a href="${LATEST_URL}">Latest</a></p>
  <p><a href="${CURRENT_URL}">Current run (${RUN_NUMBER})</a></p>
  <p><a href="./reports/">Browse runs</a></p>
</body>
</html>
EOF

{
  echo '<!doctype html>'
  echo '<html lang="en">'
  echo '<head><meta charset="utf-8" /><meta name="viewport" content="width=device-width, initial-scale=1" />'
  echo '<title>Allure Reports - Runs</title></head>'
  echo '<body>'
  echo '<h1>Allure Reports - Runs</h1>'
  echo "<p><a href=\"../latest/\">Latest</a> | <a href=\"./${RUN_NUMBER}/\">Current (${RUN_NUMBER})</a></p>"
  echo '<ul>'

  {
    ls -1 gh-pages/reports 2>/dev/null || true
    echo "${RUN_NUMBER}"
  } | sort -nr | uniq | while read -r n; do
    [ -n "$n" ] || continue
    echo "<li><a href=\"./${n}/\">Run ${n}</a></li>"
  done

  echo '</ul>'
  echo '</body></html>'
} > site/reports/index.html
