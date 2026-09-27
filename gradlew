#!/bin/sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
  echo "Missing gradle/wrapper/gradle-wrapper.jar. Run ./bootstrap-wrapper.sh first." >&2
  exit 1
fi
exec java ${JAVA_OPTS:-} ${GRADLE_OPTS:-} -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
