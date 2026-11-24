#!/usr/bin/env sh
set -eu
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
MAVEN="$ROOT/.mvn/apache-maven-3.9.11/bin/mvn"
if [ ! -x "$MAVEN" ]; then
  URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.11/apache-maven-3.9.11-bin.zip"
  echo "Descargando Apache Maven 3.9.11 (solo la primera vez)..."
  if command -v curl >/dev/null 2>&1; then curl -fsSL "$URL" -o "$ROOT/.mvn/apache-maven-3.9.11-bin.zip";
  elif command -v wget >/dev/null 2>&1; then wget -q "$URL" -O "$ROOT/.mvn/apache-maven-3.9.11-bin.zip";
  else echo "Se requiere curl o wget para descargar Maven." >&2; exit 1; fi
  unzip -q "$ROOT/.mvn/apache-maven-3.9.11-bin.zip" -d "$ROOT/.mvn"
  rm -f "$ROOT/.mvn/apache-maven-3.9.11-bin.zip"
fi
exec "$MAVEN" "$@"
