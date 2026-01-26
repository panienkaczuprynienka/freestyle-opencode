#!/bin/sh

set -euf

MVNW_VERBOSE=${MVNW_VERBOSE:-false}

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
MAVEN_PROJECTBASEDIR=${MAVEN_PROJECTBASEDIR:-"$BASE_DIR"}

WRAPPER_DIR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
PROPS_FILE="$WRAPPER_DIR/maven-wrapper.properties"
JAR_FILE="$WRAPPER_DIR/maven-wrapper.jar"

if [ ! -f "$PROPS_FILE" ]; then
  echo "Missing $PROPS_FILE" >&2
  exit 1
fi

wrapperUrl=$(grep -E '^wrapperUrl=' "$PROPS_FILE" | cut -d= -f2-)

download() {
  url="$1"
  dest="$2"

  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$url" -o "$dest"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$url" -O "$dest"
  else
    echo "Neither curl nor wget is available to download Maven Wrapper." >&2
    exit 1
  fi
}

mkdir -p "$WRAPPER_DIR"

if [ ! -f "$JAR_FILE" ]; then
  if [ "$MVNW_VERBOSE" = "true" ]; then
    echo "Downloading Maven Wrapper jar from $wrapperUrl" >&2
  fi
  download "$wrapperUrl" "$JAR_FILE"
fi

if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
else
  JAVA_EXE=$(command -v java || true)
fi

if [ -z "${JAVA_EXE:-}" ]; then
  echo "Java not found. Set JAVA_HOME or ensure 'java' is on PATH." >&2
  exit 1
fi

exec "$JAVA_EXE" \
  -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
  -classpath "$JAR_FILE" \
  org.apache.maven.wrapper.MavenWrapperMain \
  "$@"
