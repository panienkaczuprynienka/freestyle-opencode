#!/usr/bin/env bash

set -euo pipefail

mkdir -p gh-pages
rm -rf target/allure-report

REPORT_NAME=${ALLURE_REPORT_NAME:-Automation Report}

npx -y allure@3.0.1 awesome target/allure-results \
  --output target/allure-report \
  --name "$REPORT_NAME" \
  --history-path gh-pages/history.json
