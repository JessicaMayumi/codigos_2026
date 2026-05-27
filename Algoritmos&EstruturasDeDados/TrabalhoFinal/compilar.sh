#!/usr/bin/env bash
# Compila todos os fontes Java em src/ para bin/
set -euo pipefail
cd "$(dirname "$0")"
rm -rf bin
mkdir -p bin
find src -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d bin
echo "Compilação concluída em $(pwd)/bin"
