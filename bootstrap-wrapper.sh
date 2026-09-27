#!/usr/bin/env sh
set -eu
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
mkdir -p "$ROOT/gradle/wrapper"
curl -fL "https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/gradle/wrapper/gradle-wrapper.jar" \
  -o "$ROOT/gradle/wrapper/gradle-wrapper.jar"
echo "Gradle wrapper installed."
